package com.ajira.rover.ajirayaan.resource;

import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ajira.rover.ajirayaan.bean.AjirayaanRover;
import com.ajira.rover.ajirayaan.bean.Environment;
import com.ajira.rover.ajirayaan.bean.Inventory;
import com.ajira.rover.ajirayaan.bean.Location;
import com.ajira.rover.ajirayaan.bean.RoverState;
import com.ajira.rover.ajirayaan.enums.DirectionEnum;
import com.ajira.rover.ajirayaan.enums.TerrainTypeEnum;
import com.ajira.rover.ajirayaan.exceptions.RoverDeadException;
import com.ajira.rover.ajirayaan.exceptions.RoverIllegalArgumentException;
import com.ajira.rover.ajirayaan.exceptions.RoverRuntimeException;
import com.ajira.rover.ajirayaan.helper.RoverJsonParser;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

import static com.ajira.rover.ajirayaan.constants.ApplicationConstants.MAX_CAPACITY;

@RestController
@RequestMapping(value = "/api/rover")
public class RoverResource {
	private static final Logger log = LoggerFactory.getLogger(RoverResource.class);

	private static final int INVENTORY_MAX_CAPACITY = 10;

	@Autowired
	AjirayaanRover globalRoverConfig;
	@Autowired
	RoverState roverState;
	@Autowired
	private Environment globalEnvironment;
	@Autowired
	MessageSource messageSource;

	@GetMapping(value = "hello")
	public String helloRover() {
		return "Hello. I'm Ajirayaan";
	}

	@PostMapping(value = "configure")
	@ResponseStatus(value = HttpStatus.OK)
	public void configure(@RequestBody AjirayaanRover rover) {
		if (globalRoverConfig.getLocation() != null) {
			throw new RoverRuntimeException("Rover already configured. User other apis to interact with Rover.",
					HttpStatus.BAD_REQUEST);
		}
		globalRoverConfig.setBattery(rover.getBattery());
		globalRoverConfig.setInventory(rover.getInventory());
		globalRoverConfig.setLocation(rover.getLocation());
		globalRoverConfig.setScenarios(rover.getScenarios());
		globalRoverConfig.setStates(rover.getStates());
		roverState.setBattery(rover.getBattery());
		roverState.setInventory(rover.getInventory());
		roverState.setLocation(rover.getLocation());
	}

	@PostMapping(value = "move")
	@ResponseStatus(value = HttpStatus.OK)
	public void move(@RequestBody String str) {
		roverBatterCheck();
		DirectionEnum direction = RoverJsonParser.parseDirectionForString(str);

		checkForEnviromentAndRoverConfig();
		if (globalEnvironment.isStorm()) {
			throw new RoverRuntimeException(
					messageSource.getMessage("move.error.storm", null, LocaleContextHolder.getLocale()),
					HttpStatus.PRECONDITION_REQUIRED);
		} else if (isNotValid(direction)) {
			throw new RoverRuntimeException(
					messageSource.getMessage("move.error.area.crossed", null, LocaleContextHolder.getLocale()),
					HttpStatus.PRECONDITION_REQUIRED);
		}
	}

	private void checkForEnviromentAndRoverConfig() {
		if (globalRoverConfig.getLocation() == null || globalEnvironment.getAreaMap() == null) {
			throw new RoverRuntimeException(messageSource.getMessage("rover.environment.not.configured.error", null,
					LocaleContextHolder.getLocale()), HttpStatus.PRECONDITION_REQUIRED);
		}

	}

	private boolean isNotValid(DirectionEnum direction) {
		Location location = roverState.getLocation();
		int maxRow = globalEnvironment.getAreaMap().size();
		int maxColumn = globalEnvironment.getAreaMap().get(0).size();
		int index;
		switch (direction) {
		case UP:
			index = location.getRow() - 1;
			if (index < 0)
				return true;
			else
				roverState.getLocation().setRow(index);
			break;
		case DOWN:
			index = location.getRow() + 1;
			if (index >= maxRow)
				return true;
			else
				roverState.getLocation().setRow(index);
			break;
		case LEFT:
			index = location.getColumn() - 1;
			if (index < 0)
				return true;
			else
				roverState.getLocation().setColumn(index);
			break;
		case RIGHT:
			index = location.getColumn() + 1;
			if (index >= maxColumn)
				return true;
			else
				roverState.getLocation().setColumn(index);
			break;
		default:
			throw new RoverIllegalArgumentException("Invalid Direction Sent.\"" + direction + "\"",
					HttpStatus.BAD_REQUEST);
		}
		performTaskAndCollectSample();
		return false;
	}

	private void performTaskAndCollectSample() {
		log.info("performing task: {}", roverState);
		roverState.setBattery(roverState.getBattery() - 1);
		roverState.setRoverStepCount(roverState.getRoverStepCount() + 1);
		roverBatterCheck();
		Inventory inv = performAndAddTaskToInventory();
		if (inv != null) {
			roverState.getInventory().add(inv);
		}

	}

	private void removeLowPriorityItem(int count) {
		if (roverState.getInventoryCount() + count > INVENTORY_MAX_CAPACITY) {
			for (int i = 0; i < count; i++) {
				Iterator<Inventory> iter = roverState.getInventory().iterator();
				int max = 0;
				for (Inventory inv : roverState.getInventory()) {
					inv = iter.next();
					if (inv.getPriority() > max) {
						max = inv.getPriority();
					}
				}
				removeItemLogic(iter, max);
			}
		}
	}

	private void removeItemLogic(Iterator<Inventory> iter, int max) {
		Inventory curInv;
		while (iter.hasNext()) {
			curInv = iter.next();
			if (curInv.getPriority() == max) {
				if (curInv.getQuantity() > 1) {
					curInv.setQuantity(curInv.getQuantity() - 1);
				} else {
					iter.remove();
				}
				log.info("Removing Item: {}", roverState.getInventory());
				roverState.setInventoryCount(roverState.getInventoryCount() - 1);
				break;
			}
		}
	}

	private Inventory performAndAddTaskToInventory() {
		Inventory newInv = null;
		roverState.setInventoryCount(roverState.getInventoryCount() + 1);
		String terrain = globalEnvironment.getAreaMap().get(roverState.getLocation().getRow())
				.get(roverState.getLocation().getColumn());
		globalEnvironment.setTerrain(terrain);
		if (terrain.equals(TerrainTypeEnum.WATER.getValue())) {
			roverState.setInventoryCount(roverState.getInventoryCount() + 1);
			newInv = updateQuantityOrAddItem("water-sample", 2);
			if (newInv != null) {
				removeLowPriorityItem(2);
				newInv.setPriority(2);
				newInv.setQuantity(2);
			}
		} else if (terrain.equals(TerrainTypeEnum.ROCK.getValue())) {
			roverState.setInventoryCount(roverState.getInventoryCount() + 1);
			newInv = updateQuantityOrAddItem("rock-sample", 3);
			if (newInv != null) {
				removeLowPriorityItem(3);
				newInv.setPriority(3);
				newInv.setQuantity(3);
			}
		}
		return newInv;
	}

	private Inventory updateQuantityOrAddItem(String item, int count) {
		Inventory newInv = new Inventory();
		for (Inventory inv : roverState.getInventory()) {
			if (inv.getType().equalsIgnoreCase(item)) {
				inv.setQuantity(inv.getQuantity() + count);
				return null;
			}
		}
		newInv.setType(item);
		return newInv;
	}

	private void roverBatterCheck() {
		if (roverState.getRoverStepCount() >= 10) {
			roverState.setBattery(
					roverState.getBattery() + 10 >= MAX_CAPACITY ? MAX_CAPACITY : roverState.getBattery() + 10);
			roverState.setRoverStepCount(0);
		}
		if (roverState.getLocation() != null && roverState.getBattery() == 0) {
			throw new RoverDeadException();
		}

	}

	@GetMapping(value = "status")
	@ResponseStatus(value = HttpStatus.OK)
	public String status() {
		roverBatterCheck();
		checkForEnviromentAndRoverConfig();
		return RoverJsonParser.combinedString(globalEnvironment, roverState);
	}

	public MappingJacksonValue filterEnvironmentFields(String... args) {
		MappingJacksonValue map = new MappingJacksonValue(globalEnvironment);
		SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept(args);
		FilterProvider filters = new SimpleFilterProvider().addFilter("area-map-filter", filter);
		map.setFilters(filters);
		return map;

	}
}

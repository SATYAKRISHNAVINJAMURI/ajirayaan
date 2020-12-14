package com.ajira.rover.ajirayaan.resource;

import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ajira.rover.ajirayaan.bean.Environment;
import com.ajira.rover.ajirayaan.bean.Inventory;
import com.ajira.rover.ajirayaan.bean.RoverState;
import com.ajira.rover.ajirayaan.constants.ApplicationConstants;
import com.ajira.rover.ajirayaan.exceptions.RoverDeadException;
import com.ajira.rover.ajirayaan.exceptions.RoverRuntimeException;
import com.ajira.rover.ajirayaan.helper.RoverJsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

@RestController
@RequestMapping(value = "/api/environment")
public class EnvironmentResource {
	@Autowired
	private Environment globalEnvironment;
	@Autowired
	private RoverState roverState;
	@Autowired
	MessageSource messageSource;
	@GetMapping(value = "hello")
	public String helloEnvironment() {
		return "Hello. I'm Extra-terrestrial environment and expecting Ajirayaan Soon.";
	}
	
	@PostMapping(value = "configure")
	@ResponseStatus(value = HttpStatus.OK)
	public void configure(@RequestBody Environment environment) {
		if(globalEnvironment.getAreaMap() != null) {
			throw new RoverRuntimeException("Environment already configured. Send Patch request to"
					+ "/api/environment to update the environment.", HttpStatus.BAD_REQUEST);
		}
		globalEnvironment.setTemperature(environment.getTemperature());
		globalEnvironment.setHumidity(environment.getHumidity());
		globalEnvironment.setSolarFlare(environment.isSolarFlare());
		globalEnvironment.setStorm(environment.isStorm());
		globalEnvironment.setAreaMap(environment.getAreaMap());
	}
	
	private void checkForEnviromentConfig() {
		if(globalEnvironment.getAreaMap() == null) {
			throw new RoverRuntimeException(messageSource
					.getMessage("environment.not.configured.error", null,LocaleContextHolder.getLocale()),HttpStatus.PRECONDITION_REQUIRED);
		}
		
	}
	
	@PatchMapping
	public void applyPatchToEnvironment(@RequestBody String jsonString) {
		roverBatterCheck();
		checkForEnviromentConfig();
		TreeNode treeNode = RoverJsonParser.parseJson(jsonString);
		@SuppressWarnings("unchecked")
		Class<List<List<String>>> clazz = (Class<List<List<String>>>) globalEnvironment.getAreaMap().getClass();
		try {
			if(treeNode.get(ApplicationConstants.TEMPARATURE) != null) {
				globalEnvironment.setTemperature(Integer.parseInt(treeNode.get(ApplicationConstants.TEMPARATURE).toString()));
			}
			if(treeNode.get(ApplicationConstants.HUMIDITY) != null) {
				globalEnvironment.setHumidity(Integer.parseInt(treeNode.get(ApplicationConstants.HUMIDITY).toString()));
			}
			if(treeNode.get(ApplicationConstants.SOLAR_FLARE) != null) {
				if(Boolean.parseBoolean(treeNode.get(ApplicationConstants.SOLAR_FLARE).toString())) {
					roverState.setBattery(ApplicationConstants.MAX_CAPACITY);
				}
				globalEnvironment.setSolarFlare(Boolean.parseBoolean(treeNode.get(ApplicationConstants.SOLAR_FLARE).toString()));
			}
			if(treeNode.get(ApplicationConstants.STORM) != null) {
				globalEnvironment.setStorm(Boolean.parseBoolean(treeNode.get(ApplicationConstants.STORM).toString()));
				if(globalEnvironment.isStorm()) {
					sheildRoverAndRemoveSheild();
				}
			}
			if(treeNode.get(ApplicationConstants.AREA_MAP) != null) {
				globalEnvironment.setAreaMap(RoverJsonParser.fromJsonToObject(treeNode.get(ApplicationConstants.AREA_MAP)
						, clazz));
			}
		}catch (NumberFormatException ex) {
			throw new RoverRuntimeException(ex.getMessage(), HttpStatus.BAD_REQUEST);
		}

	}
	private void sheildRoverAndRemoveSheild() {
		Iterator<Inventory> iter = roverState.getInventory().iterator();
		Inventory curInv;
		boolean isPresent = false;
		while(iter.hasNext()) {
			curInv = iter.next();
			if(curInv.getType().equalsIgnoreCase(ApplicationConstants.STORM_SHELD)) {
				if(curInv.getQuantity() > 1) {
					curInv.setQuantity(curInv.getQuantity() - 1);
				}else {
					iter.remove();
				}
				isPresent = true;
				roverState.setInventoryCount(roverState.getInventoryCount() - 1);
				break;
			}
		}
		if(!isPresent) {
			roverState.setBattery(0);
			throw new RoverRuntimeException("There are no Storm fields in the Rover. Rover Destroyed", HttpStatus.CONFLICT);
		}
	}

	private void roverBatterCheck() {
		if(roverState.getLocation() != null && roverState.getBattery() == 0) {
			throw new RoverDeadException();
		}
		
	}
	
	public MappingJacksonValue filterEnvironmentFields(String... args) {
		MappingJacksonValue map = new MappingJacksonValue(globalEnvironment);
		SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter
				.filterOutAllExcept(args);
		FilterProvider filters = new SimpleFilterProvider().addFilter("area-map-filter", filter);
		map.setFilters(filters);
		return map;
		
	}
}

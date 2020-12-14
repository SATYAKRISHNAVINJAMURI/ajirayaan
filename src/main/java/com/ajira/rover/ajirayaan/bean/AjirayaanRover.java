package com.ajira.rover.ajirayaan.bean;

import java.util.List;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;

@Component(value = "globalRoverConfig")
public class AjirayaanRover {
	@JsonProperty("initial-battery")
	private int battery;
	@JsonProperty("deploy-point")
	private Location location;
	private List<Inventory> inventory;
	private List<State> states;
	private List<Scenario> scenarios;
	public int getBattery() {
		return battery;
	}
	public void setBattery(int battery) {
		this.battery = battery;
	}
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	public List<State> getStates() {
		return states;
	}
	public void setStates(List<State> states) {
		this.states = states;
	}
	public List<Scenario> getScenarios() {
		return scenarios;
	}
	public void setScenarios(List<Scenario> scenarios) {
		this.scenarios = scenarios;
	}
	public List<Inventory> getInventory() {
		return inventory;
	}
	public void setInventory(List<Inventory> inventory) {
		this.inventory = inventory;
	}
	
}

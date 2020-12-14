package com.ajira.rover.ajirayaan.bean;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Scenario {
	private String name;
	@JsonProperty("conditions")
	private List<Condition> conditions;
	@JsonProperty("rover")
	private List<Rover> rover;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Condition> getConditions() {
		return conditions;
	}
	public void setConditions(List<Condition> conditions) {
		this.conditions = conditions;
	}
	public List<Rover> getRover() {
		return rover;
	}
	public void setRover(List<Rover> rover) {
		this.rover = rover;
	}
	
	
}

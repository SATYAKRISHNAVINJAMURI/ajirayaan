package com.ajira.rover.ajirayaan.bean;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class State {
	private String name;
	@JsonProperty("allowedActions")
	private List<String> allowedActions;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<String> getAllowedActions() {
		return allowedActions;
	}
	public void setAllowedActions(List<String> allowedActions) {
		this.allowedActions = allowedActions;
	}
}

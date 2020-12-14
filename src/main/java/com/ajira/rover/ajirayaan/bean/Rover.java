package com.ajira.rover.ajirayaan.bean;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Rover {
	@JsonProperty("performs")
	private Performs performs;
	private String is;
	
	public Performs  getPerforms() {
		return performs;
	}
	public void setPerforms(Performs  performs) {
		this.performs = performs;
	}
	public String getIs() {
		return is;
	}
	public void setIs(String is) {
		this.is = is;
	}
	
	

}

package com.ajira.rover.ajirayaan.bean;

import java.util.List;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonProperty;

@Component("globalEnvironment")
@JsonFilter("area-map-filter")
public class Environment {
	private int temperature;
	private int humidity;
	@JsonProperty("solar-flare")
	private boolean solarFlare;
	private boolean storm;
	@JsonProperty("area-map")
	private List<List<String>> areaMap;
	private String terrain;
	
	public String getTerrain() {
		return terrain;
	}
	public void setTerrain(String terrain) {
		this.terrain = terrain;
	}
	public int getTemperature() {
		return temperature;
	}
	public void setTemperature(int temperature) {
		this.temperature = temperature;
	}
	public int getHumidity() {
		return humidity;
	}
	public void setHumidity(int humidity) {
		this.humidity = humidity;
	}
	public boolean isSolarFlare() {
		return solarFlare;
	}
	public void setSolarFlare(boolean solarFlare) {
		this.solarFlare = solarFlare;
	}
	public boolean isStorm() {
		return storm;
	}
	public void setStorm(boolean storm) {
		this.storm = storm;
	}
	public List<List<String>> getAreaMap() {
		return areaMap;
	}
	public void setAreaMap(List<List<String>> areaMap) {
		this.areaMap = areaMap;
	}
	@Override
	public String toString() {
		return "Environment [temparature=" + temperature + ", humidity=" + humidity + ", solarFlare=" + solarFlare
				+ ", storm=" + storm + "]";
	}	
}

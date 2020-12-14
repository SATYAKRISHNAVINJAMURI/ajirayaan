package com.ajira.rover.ajirayaan.bean;

import java.util.List;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Component(value = "roverState")
public class RoverState {
	private Location location;
	private int battery;
	private List<Inventory> inventory;
	@JsonIgnore
	private int inventoryCount;
	@JsonIgnore
	private int roverStepCount;
	
	public int getRoverStepCount() {
		return roverStepCount;
	}
	public void setRoverStepCount(int roverStepCount) {
		this.roverStepCount = roverStepCount;
	}
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	public int getBattery() {
		return battery;
	}
	public void setBattery(int battery) {
		this.battery = battery;
	}
	public List<Inventory> getInventory() {
		return inventory;
	}
	public void setInventory(List<Inventory> inventory) {
		this.inventory = inventory;
		for(Inventory inv : inventory) {
			inventoryCount += inv.getQuantity();
		}
	}
	public int getInventoryCount() {
		return inventoryCount;
	}
	public void setInventoryCount(int inventoryCount) {
		this.inventoryCount = inventoryCount;
	}
	@Override
	public String toString() {
		return "RoverState [location=" + location + ", battery=" + battery + ", inventory=" + inventory
				+ ", inventoryCount=" + inventoryCount + ", roverStepCount=" + roverStepCount + "]";
	}	
}

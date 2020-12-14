package com.ajira.rover.ajirayaan.enums;

public enum TerrainTypeEnum {
	DIRT("dirt"), WATER("water"), ROCK("rock"), SAND("sand");

	private String value;
	private TerrainTypeEnum(String string) {
		this.value = string;
	}
	public String getValue() {
		return value;
	}
}

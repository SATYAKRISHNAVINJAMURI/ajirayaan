package com.ajira.rover.ajirayaan.enums;

public enum DirectionEnum {
	UP("up"), DOWN("down"), LEFT("left"), RIGHT("right");
	private String value;
	private DirectionEnum(String string) {
		this.value = string;
	}
	public String getValue() {
		return value;
	}
}

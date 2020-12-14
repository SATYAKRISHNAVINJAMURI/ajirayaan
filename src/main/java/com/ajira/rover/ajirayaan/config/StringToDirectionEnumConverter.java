package com.ajira.rover.ajirayaan.config;

import org.springframework.core.convert.converter.Converter;

import com.ajira.rover.ajirayaan.enums.DirectionEnum;

public class StringToDirectionEnumConverter implements Converter<String, DirectionEnum> {
	@Override
	public DirectionEnum convert(String str) {
		return DirectionEnum.valueOf(str.toUpperCase());
	}

}

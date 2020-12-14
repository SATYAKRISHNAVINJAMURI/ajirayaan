package com.ajira.rover.ajirayaan.config;

import org.springframework.core.convert.converter.Converter;

import com.ajira.rover.ajirayaan.enums.TerrainTypeEnum;

public class StringToTerrainTypeEnumConverter implements Converter<String, TerrainTypeEnum> {
	@Override
	public TerrainTypeEnum convert(String str) {
		return TerrainTypeEnum.valueOf(str.toUpperCase());
	}

}

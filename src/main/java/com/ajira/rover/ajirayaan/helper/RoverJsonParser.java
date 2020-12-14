package com.ajira.rover.ajirayaan.helper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;

import com.ajira.rover.ajirayaan.bean.Environment;
import com.ajira.rover.ajirayaan.bean.RoverState;
import com.ajira.rover.ajirayaan.constants.ApplicationConstants;
import com.ajira.rover.ajirayaan.enums.DirectionEnum;
import com.ajira.rover.ajirayaan.exceptions.RoverRuntimeException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

public class RoverJsonParser {
	private static ObjectMapper objectMapper;

	private static ObjectMapper getObjectMapper() {
		if (objectMapper == null) {
			return new ObjectMapper();
		} else {
			return objectMapper;
		}
	}

	public static TreeNode parseJson(String jsonString) {
		TreeNode tree = null;
		try {
			tree = getObjectMapper().readTree(jsonString);
		} catch (JsonProcessingException e) {
			throw new RoverRuntimeException("Bad Json Format", HttpStatus.BAD_REQUEST);
		}
		return tree;
	}

	public static <T> T fromJsonToObject(String jsonString, String jsonProperty, Class<T> clazz) {
		TreeNode tree = null;
		try {
			tree = getObjectMapper().readTree(jsonString);
			return getObjectMapper().treeToValue(tree.get(jsonProperty), clazz);
		} catch (JsonProcessingException e) {
			throw new RoverRuntimeException("Bad Json Format", HttpStatus.BAD_REQUEST);
		}
	}

	public static <T> T fromJsonToObject(TreeNode tree, Class<T> clazz) {
		try {
			return getObjectMapper().treeToValue(tree, clazz);
		} catch (JsonProcessingException e) {
			throw new RoverRuntimeException("Bad Json Format", HttpStatus.BAD_REQUEST);
		}
	}

	public static void main(String[] args) {
		String newjson = "{\"temparature\": 20}";
		List<List<String>> list = new ArrayList<>();
		String jsonString = "{\"temperature\": 70,\"humidity\": 65,\"solar-flare\": true,\"storm\": true,\"area-map\": [[ \"dirt\", \"dirt\" ],[ \"dirt\", \"dirt\" ]]}";
		System.out.println(fromJsonToObject(jsonString, "area-map", list.getClass()));
		System.out.println(parseJson(jsonString).get("alsjkdf"));
	}

	public static String combinedString(Environment env, RoverState rover) {
		StringBuilder str = new StringBuilder("{");
		try {
			str.append("rover:");
			str.append(getObjectMapper().writeValueAsString(rover));
			str.append(",");
			str.append("environment:");
		    SimpleFilterProvider filterProvider = new SimpleFilterProvider().addFilter("area-map-filter",
		            SimpleBeanPropertyFilter.filterOutAllExcept("temperature","humidity","solar-flare","storm","terrain"));
			str.append(getObjectMapper().writer(filterProvider).writeValueAsString(env));
			str.append("}");
		} catch (JsonProcessingException e) {
			throw new RoverRuntimeException("Error while Parsing Json String Out fo object",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return str.toString();
	}

	public static DirectionEnum parseDirectionForString(String str) {
		return DirectionEnum.valueOf(RoverJsonParser.parseJson(str).
		get(ApplicationConstants.DIRECTION).toString().replace("\"", "").toUpperCase());
	}
}

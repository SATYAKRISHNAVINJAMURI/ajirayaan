package com.ajira.rover.ajirayaan.exceptions;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/*
 * Exception Template that will be followed by all the
 * exception responses that occurred while process
 * a request on this resource.
 */
public class ExceptionResponse {
	private static final Logger log = LoggerFactory.getLogger(ExceptionResponse.class);
	private String message;
	
	public ExceptionResponse(Date date, String message, String description) {
		super();
		log.error("Exception occurred at time: {}\nmessage: {}\ndescription: {}",
				date,message,description);
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}

package com.ajira.rover.ajirayaan.exceptions;

import org.springframework.http.HttpStatus;

public class RoverRuntimeException extends RuntimeException {
	static final long serialVersionUID = -7034897190745766939L;
	private String message;
	private HttpStatus httpStatus;
	
	public RoverRuntimeException(String message, HttpStatus httpStatus) {
		super();
		this.message = message;
		this.httpStatus = httpStatus;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public HttpStatus getHttpStatus() {
		return httpStatus;
	}
	public void setHttpStatus(HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}

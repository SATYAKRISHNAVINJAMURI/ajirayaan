package com.ajira.rover.ajirayaan.exceptions;

import java.util.Date;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@RestController
public class RoverAryaanExceptionHandler
extends ResponseEntityExceptionHandler{
	
	@ExceptionHandler(Exception.class)
	public final ResponseEntity<Object> handleAllException(Exception ex, WebRequest request) throws Exception {
		return new ResponseEntity<>(new ExceptionResponse(new Date(), ex.getMessage(), 
				request.getDescription(false)) , HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(RoverRuntimeException.class)
	public final ResponseEntity<Object> handleCustomizedRunTimeException(RoverRuntimeException ex, WebRequest request) throws Exception {
		return new ResponseEntity<>(new ExceptionResponse(new Date(), ex.getMessage(), 
				request.getDescription(false)) , ex.getHttpStatus());
	}
	
	@ExceptionHandler(RoverIllegalArgumentException.class)
	public final ResponseEntity<Object> handleCustomizedRoverIllegalArgumentException(RoverIllegalArgumentException ex, WebRequest request) throws Exception {
		return new ResponseEntity<>(new ExceptionResponse(new Date(), ex.getMessage(), 
				request.getDescription(false)) , ex.getHttpStatus());
	}
	
	@ExceptionHandler(RoverDeadException.class)
	public final ResponseEntity<Object> handleRoverDeadException(RoverDeadException ex, WebRequest request) {
		return new ResponseEntity<>(new ExceptionResponse(new Date(), "Rover Dead", 
				request.getDescription(false)) , HttpStatus.GONE);
	}
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
			MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		return new ResponseEntity<>(new ExceptionResponse(new Date(), "Validation Failed", 
				ex.getBindingResult().toString()) , status);
	}
	

	
}

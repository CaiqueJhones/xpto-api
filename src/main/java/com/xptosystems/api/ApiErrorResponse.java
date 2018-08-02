package com.xptosystems.api;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiErrorResponse {

	private HttpStatus status = HttpStatus.BAD_REQUEST;
	private String message;
	private List<String> errors;
	
	public ApiErrorResponse() {}
	
	public ApiErrorResponse(String message) {
		this.message = message;
		errors = new ArrayList<>();
	}
	
	public void addError(String error) {
	    errors.add(error);
	}
}

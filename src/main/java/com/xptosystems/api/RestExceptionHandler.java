package com.xptosystems.api;

import java.util.Arrays;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolationException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class RestExceptionHandler {
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	ResponseEntity<ApiErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
		BindingResult result = ex.getBindingResult();
		ApiErrorResponse apiErrorResponse = new ApiErrorResponse("Erro na validação");
		result.getFieldErrors().forEach(field -> {
			apiErrorResponse.addError(String.format("%s: %s", field.getField(), field.getDefaultMessage()));
		});
		
		result.getGlobalErrors().forEach(obj -> {
			apiErrorResponse.addError(obj.getDefaultMessage());
		});
		return new ResponseEntity<>(apiErrorResponse, apiErrorResponse.getStatus());
	}
	
	@ExceptionHandler(ConstraintViolationException.class)
	ResponseEntity<ApiErrorResponse> handleConstraintViolation(ConstraintViolationException ex,
	        WebRequest request) {
		val errors = ex.getConstraintViolations().stream()
		        .map(c -> c.getPropertyPath() + " " + c.getMessage()).collect(Collectors.toList());
		val message = ex.getMessage();
		val apiError = new ApiErrorResponse(HttpStatus.BAD_REQUEST, message, errors);
		return new ResponseEntity<>(apiError, apiError.getStatus());
	}
	
	@ExceptionHandler(DataIntegrityViolationException.class)
	ResponseEntity<ApiErrorResponse> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
		val apiError = new ApiErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(),
		        Arrays.asList("Constraint Violation: " + ex.getMessage()));
		return new ResponseEntity<>(apiError, apiError.getStatus());
	}
	
	@ExceptionHandler(NotFoundException.class)
    ResponseEntity<ApiErrorResponse> handleNotFound(NotFoundException ex, WebRequest request) {
        val apiError = new ApiErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), Arrays.asList(ex.getMessage()));
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
	
	@ExceptionHandler(MissingServletRequestParameterException.class)
    ResponseEntity<ApiErrorResponse> handleMissingServletRequestParameterException(MissingServletRequestParameterException ex, WebRequest request) {
        val apiError = new ApiErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), Arrays.asList(ex.getMessage()));
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

	@ExceptionHandler({ Exception.class, RuntimeException.class })
	ResponseEntity<ApiErrorResponse> handleGenericException(Exception ex, WebRequest request) {
		log.error(ex.getMessage(), ex);
		val apiError = new ApiErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage(),
				Arrays.asList(ex.getMessage()));
		return new ResponseEntity<>(apiError, apiError.getStatus());
	}
}

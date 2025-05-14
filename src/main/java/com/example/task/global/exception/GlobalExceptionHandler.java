package com.example.task.global.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

	@ExceptionHandler(NoHandlerFoundException.class)
	public ResponseEntity<Map<String, Object>> handleNotFound(NotFoundException e) {
		return createErrorResponse(e.getErrorCode());
	}

	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<Map<String, Object>> handleBadRequest(BadRequestException e) {
		return createErrorResponse(e.getErrorCode());
	}

	@ExceptionHandler(UnauthorizedException.class)
	public ResponseEntity<Map<String, Object>> handleUnauthorized(UnauthorizedException e) {
		return createErrorResponse(e.getErrorCode());
	}

	@ExceptionHandler(ConflictException.class)
	public ResponseEntity<Map<String, Object>> handleConflict(ConflictException e) {
		return createErrorResponse(e.getErrorCode());
	}


	private ResponseEntity<Map<String, Object>> createErrorResponse(ErrorCode errorCode) {
		Map<String, Object> response = new HashMap<>();
		response.put("code", errorCode.name());
		response.put("message", errorCode.getMessage());
		response.put("status", errorCode.getStatus().value());
		return ResponseEntity.status(errorCode.getStatus()).body(response);
	}

}

package com.example.task.global.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class BadRequestException extends RuntimeException {
	private final ErrorCode errorCode;

	public BadRequestException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}

	public HttpStatus getHttpStatus() {
		return errorCode.getStatus();
	}
}

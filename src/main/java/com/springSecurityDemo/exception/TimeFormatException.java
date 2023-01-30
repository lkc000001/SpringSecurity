package com.springsecuritydemo.exception;

public class TimeFormatException extends RuntimeException {
	
	private final Integer code;
	
	public TimeFormatException(String message, Integer code) {
		super(message);
		this.code = code;
	}

	public Integer getCode() {
		return code;
	}
}

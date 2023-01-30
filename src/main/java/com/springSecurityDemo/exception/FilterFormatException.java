package com.springsecuritydemo.exception;

public class FilterFormatException extends RuntimeException {
	
	private final Integer code;
	
	public FilterFormatException(String message, Integer code) {
		super(message);
		this.code = code;
	}

	public Integer getCode() {
		return code;
	}
}

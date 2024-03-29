package com.springsecuritydemo.exception;

public class AesException extends RuntimeException {
	
	private final Integer code;
	
	public AesException(String message, Integer code) {
		super(message);
		this.code = code;
	}

	public Integer getCode() {
		return code;
	}
	
}

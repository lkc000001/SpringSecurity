package com.springsecuritydemo.exception;

public class LoginErrorException extends RuntimeException {
	
	private final Integer code;
	
	public LoginErrorException(String message, Integer code) {
		super(message);
		this.code = code;
	}

	public Integer getCode() {
		return code;
	}
}

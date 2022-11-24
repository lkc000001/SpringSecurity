package com.springSecurityDemo.exception;

public class AuthCodeException extends RuntimeException {
	
	private Integer code;
	
	public AuthCodeException(String message, Integer code) {
		super(message);
		this.code = code;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}
	
}

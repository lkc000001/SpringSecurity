package com.springsecuritydemo.exception;

public class PermissionNullException extends RuntimeException {
	
	private final Integer code;
	
	public PermissionNullException(String message, Integer code) {
		super(message);
		this.code = code;
	}

	public Integer getCode() {
		return code;
	}
}

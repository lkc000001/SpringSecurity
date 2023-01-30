package com.springsecuritydemo.exception;

public class QueryNoDataException extends RuntimeException {
	
	private final Integer code;
	
	public QueryNoDataException(String message, Integer code) {
		super(message);
		this.code = code;
	}

	public Integer getCode() {
		return code;
	}
}

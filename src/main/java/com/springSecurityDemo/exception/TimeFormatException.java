package com.springSecurityDemo.exception;

public class TimeFormatException extends RuntimeException {
	
	private Integer code;
	
	public TimeFormatException(String message, Integer code) {
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

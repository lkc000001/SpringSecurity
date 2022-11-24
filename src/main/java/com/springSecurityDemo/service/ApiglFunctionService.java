package com.springSecurityDemo.service;

import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import com.springSecurityDemo.entity.ApiGLFunction;

public interface ApiglFunctionService {
	
	ResponseEntity<?> queryApiglFunction(ApiGLFunction apiGLFunction) throws ParseException;

	ApiGLFunction findByApiglFunctionId(Long apiglFunctionId);
	
	String save(ApiGLFunction apiGLFunction, String func, final Authentication authentication);
	
	void delete(Long id);
	
	List<ApiGLFunction> findAll();
}

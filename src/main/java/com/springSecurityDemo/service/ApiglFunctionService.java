package com.springsecuritydemo.service;

import java.text.ParseException;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import com.springsecuritydemo.entity.ApiGLFunction;
import com.springsecuritydemo.entity.response.JSGridReturnData;

public interface ApiglFunctionService {
	
	ResponseEntity<JSGridReturnData<ApiGLFunction>> queryApiglFunction(ApiGLFunction apiGLFunction) throws ParseException;

	ApiGLFunction findByApiglFunctionId(Long apiglFunctionId);
	
	String save(ApiGLFunction apiGLFunction, String func, final Authentication authentication);
	
	void delete(Long id);
	
	List<ApiGLFunction> findAll();
}

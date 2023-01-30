package com.springsecuritydemo.service;

import java.text.ParseException;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.springsecuritydemo.entity.ApiGLRole;
import com.springsecuritydemo.entity.response.JSGridReturnData;

public interface ApiglRoleService {

	ResponseEntity<JSGridReturnData<ApiGLRole>> queryApiGLRole(ApiGLRole apiGLRole) throws ParseException;

	ApiGLRole findByApiglRoleId(Long apiglRoleId);
	
	String save(ApiGLRole apiGLRole, String func, final Authentication authentication) throws JsonProcessingException;
	
	void delete(Long id);
}

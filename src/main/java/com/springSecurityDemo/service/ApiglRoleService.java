package com.springSecurityDemo.service;

import java.text.ParseException;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import com.springSecurityDemo.entity.ApiGLRole;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface ApiglRoleService {

	ResponseEntity<?> queryApiGLRole(ApiGLRole apiGLRole) throws ParseException;

	ApiGLRole findByApiglRoleId(Long apiglRoleId);
	
	String save(ApiGLRole apiGLRole, String func, final Authentication authentication) throws JsonProcessingException;
	
	void delete(Long id);
}

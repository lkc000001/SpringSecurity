package com.springsecuritydemo.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.springsecuritydemo.entity.AppUser;
import com.springsecuritydemo.entity.PermissionResponse;
import com.springsecuritydemo.repositories.UserFunctionRepository;
import com.springsecuritydemo.service.UserService;
import com.springsecuritydemo.util.ValidateUtil;

@ControllerAdvice
public class GlobalController {

	@Autowired
	UserService userService;
	
	@Autowired
	UserFunctionRepository userFunctionRepository;
	
	@Autowired
	ValidateUtil validateUtil;
	
	@ModelAttribute(name = "functions")
	public Map<String,List<PermissionResponse>> getFunctions(final Authentication authentication) {
		return userService.getFunctions(authentication);
	}
	
	@ModelAttribute(name = "mainFunction")
	public List<String> getMainFunctions() {
		return Arrays.asList("業務", "管理", "設定");
	}
	
	@ModelAttribute(name = "appUser")
	public AppUser getAppUser(final Authentication authentication) {
		return userService.getSecurityUser(authentication);
	}
	
}

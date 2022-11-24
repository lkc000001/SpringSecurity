package com.springSecurityDemo.controller;

import static java.util.stream.Collectors.groupingBy;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;

import com.springSecurityDemo.entity.AppUser;
import com.springSecurityDemo.entity.PermissionResponse;
import com.springSecurityDemo.exception.ValidateCodeException;
import com.springSecurityDemo.repositories.UserFunctionRepository;
import com.springSecurityDemo.service.UserService;
import com.springSecurityDemo.util.ValidateUtil;

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

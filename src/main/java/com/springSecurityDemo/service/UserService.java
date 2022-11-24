package com.springSecurityDemo.service;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import com.springSecurityDemo.entity.AppUser;
import com.springSecurityDemo.entity.PermissionResponse;

public interface UserService {
	
	AppUser getSecurityUser(final Authentication authentication);
	
	ResponseEntity<?> queryUserPermission(AppUser user) throws ParseException;
	
	AppUser findByUserId(Long userId);
	
	String save(AppUser user, String func, final Authentication authentication);
	
	Map<String,List<PermissionResponse>> getFunctions(final Authentication authentication);
}

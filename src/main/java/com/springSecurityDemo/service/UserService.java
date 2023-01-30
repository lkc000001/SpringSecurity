package com.springsecuritydemo.service;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import com.springsecuritydemo.entity.AppUser;
import com.springsecuritydemo.entity.PermissionResponse;
import com.springsecuritydemo.entity.response.JSGridReturnData;

public interface UserService {
	
	AppUser getSecurityUser(final Authentication authentication);
	
	String getSecurityUserName(final Authentication authentication);
	
	ResponseEntity<JSGridReturnData<AppUser>> queryUserPermission(AppUser user) throws ParseException;
	
	AppUser findByUserId(Long userId);
	
	String save(AppUser user, String func, final Authentication authentication);
	
	Map<String,List<PermissionResponse>> getFunctions(final Authentication authentication);
}

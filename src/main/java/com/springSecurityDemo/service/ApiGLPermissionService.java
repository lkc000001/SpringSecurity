package com.springsecuritydemo.service;

import java.util.Set;

import org.springframework.security.core.Authentication;

import com.springsecuritydemo.entity.PermissionResponse;
import com.springsecuritydemo.entity.RoleFunction;
import com.springsecuritydemo.entity.UserFunction;

public interface ApiGLPermissionService {
	
	Set<PermissionResponse> queryRolePermission(Long apiglRoleId);
	
	String saveRoleFunctions(Set<RoleFunction> roleFunctions, final Authentication authentication);
	
	void deleteRoleFunctions(Long apiglRoleId);
	
	Set<PermissionResponse> queryUserPermission(Long userId);
	
	String saveUserFunctions(Set<UserFunction> userIds, final Authentication authentication);
	
	void deleteUserFunctions(Long userId);
	
}

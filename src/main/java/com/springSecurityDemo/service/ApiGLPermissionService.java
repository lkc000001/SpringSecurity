package com.springSecurityDemo.service;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;

import com.springSecurityDemo.entity.RoleFunction;
import com.springSecurityDemo.entity.UserFunction;
import com.springSecurityDemo.entity.PermissionResponse;

public interface ApiGLPermissionService {
	
	Set<PermissionResponse> queryRolePermission(Long apiglRoleId);
	
	String saveRoleFunctions(Set<RoleFunction> roleFunctions, final Authentication authentication);
	
	void deleteRoleFunctions(Long apiglRoleId);
	
	Set<PermissionResponse> queryUserPermission(Long userId);
	
	String saveUserFunctions(Set<UserFunction> userIds, final Authentication authentication);
	
	void deleteUserFunctions(Long userId);
	
}

package com.springSecurityDemo.service.impl;

import static java.util.stream.Collectors.groupingBy;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springSecurityDemo.entity.AppUser;
import com.springSecurityDemo.entity.RoleFunction;
import com.springSecurityDemo.entity.UserFunction;
import com.springSecurityDemo.entity.PermissionResponse;
import com.springSecurityDemo.exception.PermissionNullException;
import com.springSecurityDemo.repositories.RoleFunctionRepository;
import com.springSecurityDemo.repositories.UserFunctionRepository;
import com.springSecurityDemo.repositories.UserRepository;
import com.springSecurityDemo.service.ApiGLPermissionService;
import com.springSecurityDemo.service.UserService;
import com.springSecurityDemo.util.ValidateUtil;
import com.baomidou.dynamic.datasource.annotation.DS;


@DS("DBAPIGL")
@Service
public class ApiGLPermissionServiceImpl implements ApiGLPermissionService {

	@Autowired
	RoleFunctionRepository roleFunctionRepository;
	
	@Autowired
	UserFunctionRepository userFunctionRepository;
	
	@Autowired
	UserService userService;

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	ValidateUtil validateUtil;
	
	
	@Override
	public Set<PermissionResponse> queryRolePermission(Long apiglRoleId) {
    	return roleFunctionRepository.queryRolePermission(apiglRoleId);
	}
    
	@Override
	@Transactional
	public String saveRoleFunctions(Set<RoleFunction> roleFunctions, final Authentication authentication) {
		roleFunctions.forEach(role -> {
			role.setCreateTime(new Date());
			role.setCreateUser(userService.getSecurityUser(authentication).getAccount());
		});
		deleteRoleFunctions(roleFunctions.iterator().next().getApiglRoleId());
		Iterator<RoleFunction> rolefunctions = roleFunctionRepository.saveAll(roleFunctions).iterator();
		return "修改成功";
	}
	
	@Override
	public void deleteRoleFunctions(Long apiglRoleId) {
		roleFunctionRepository.deleteByapiglRoleId(apiglRoleId);
	}

	@Override
	public Set<PermissionResponse> queryUserPermission(Long userId) {
    	return userFunctionRepository.queryUserPermission(userId);
	}

	@Override
	@Transactional
	public String saveUserFunctions(Set<UserFunction> userIds, final Authentication authentication) {
		userIds.forEach(user -> {
			user.setCreateTime(new Date());
			user.setCreateUser(userService.getSecurityUser(authentication).getAccount());
		});
		deleteUserFunctions(userIds.iterator().next().getUserId());
		Iterator<UserFunction> userfunctions = userFunctionRepository.saveAll(userIds).iterator();
		return "修改成功";
	}

	@Override
	public void deleteUserFunctions(Long userId) {
		userFunctionRepository.deleteByUserId(userId);
	}
	
	public Map<String,List<PermissionResponse>> getPermissionMap(AppUser user) {

		if (validateUtil.isEmpty(user)) {
			return null;
		}
		
    	Map<String,List<PermissionResponse>> PermissionList = null;
    	
    	Set<PermissionResponse> UserPermissions = queryUserPermission(user.getUserId());
    	if (validateUtil.isNotEmpty(UserPermissions)) {
    		if(user.getGroupName().equals("DC")) {
    			//"DC"全功能都可以使用,不過濾UserFunction Enabled
    			UserPermissions = UserPermissions.stream()
								  .filter(u -> u.getEnabled() != null) 
								  .filter(u -> u.getEnabled().equals("1"))
								  .collect(Collectors.toSet());
    		} else {
    			UserPermissions = UserPermissions.stream()
							  	  .filter(u -> u.getEnabled() != null) //過濾Function Enabled的null
							 	  .filter(u -> u.getEnabled().equals("1")) //過濾Function 不啟用的功能
				    			  .filter(u -> u.getPermissionenabled() != null) //過濾UserFunction Enabled的null
								  .filter(u -> u.getPermissionenabled().equals("1")) //過濾UserFunction 沒有權限的功能
			    				  .collect(Collectors.toSet());
    		}
    		//分群處理不同類別
    		PermissionList = UserPermissions.stream()
							 .sorted(Comparator.comparing(PermissionResponse::getApiglfunctionsort))
							 .collect(groupingBy(PermissionResponse::getType, LinkedHashMap::new, Collectors.toList()));
			
    	}
    	return PermissionList;
	}
}

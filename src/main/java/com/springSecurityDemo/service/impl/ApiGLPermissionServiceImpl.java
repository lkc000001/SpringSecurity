package com.springsecuritydemo.service.impl;

import static java.util.stream.Collectors.groupingBy;

import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.springsecuritydemo.entity.AppUser;
import com.springsecuritydemo.entity.PermissionResponse;
import com.springsecuritydemo.entity.RoleFunction;
import com.springsecuritydemo.entity.UserFunction;
import com.springsecuritydemo.repositories.RoleFunctionRepository;
import com.springsecuritydemo.repositories.UserFunctionRepository;
import com.springsecuritydemo.service.ApiGLPermissionService;
import com.springsecuritydemo.service.UserService;
import com.springsecuritydemo.util.ValidateUtil;


@DS("DBAPIGL")
@Service
public class ApiGLPermissionServiceImpl implements ApiGLPermissionService {

	@Autowired
	private RoleFunctionRepository roleFunctionRepository;
	
	@Autowired
	private UserFunctionRepository userFunctionRepository;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ValidateUtil validateUtil;
	
	
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
		roleFunctionRepository.saveAll(roleFunctions).iterator();
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
		userFunctionRepository.saveAll(userIds).iterator();
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
		
    	Map<String,List<PermissionResponse>> permissionList = null;
    	
    	Set<PermissionResponse> userPermissions = queryUserPermission(user.getUserId());
    	if (validateUtil.isNotEmpty(userPermissions)) {
    		if(user.getGroupName().equals("DC")) {
    			//"DC"全功能都可以使用,不過濾UserFunction Enabled
    			userPermissions = userPermissions.stream()
								  .filter(u -> u.getEnabled() != null) 
								  .filter(u -> u.getEnabled().equals("1"))
								  .collect(Collectors.toSet());
    		} else {
    			userPermissions = userPermissions.stream()
							  	  .filter(u -> u.getEnabled() != null) //過濾Function Enabled的null
							 	  .filter(u -> u.getEnabled().equals("1")) //過濾Function 不啟用的功能
				    			  .filter(u -> u.getPermissionenabled() != null) //過濾UserFunction Enabled的null
								  .filter(u -> u.getPermissionenabled().equals("1")) //過濾UserFunction 沒有權限的功能
			    				  .collect(Collectors.toSet());
    		}
    		//分群處理不同類別
    		permissionList = userPermissions.stream()
							 .sorted(Comparator.comparing(PermissionResponse::getApiglfunctionsort))
							 .collect(groupingBy(PermissionResponse::getType, LinkedHashMap::new, Collectors.toList()));
			
    	}
    	return permissionList;
	}
}

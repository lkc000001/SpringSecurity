package com.springsecuritydemo.service.impl;

import java.text.ParseException;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.springsecuritydemo.entity.ApiGLRole;
import com.springsecuritydemo.entity.response.JSGridResponse;
import com.springsecuritydemo.entity.response.JSGridReturnData;
import com.springsecuritydemo.exception.QueryNoDataException;
import com.springsecuritydemo.repositories.ApiglRoleRepository;
import com.springsecuritydemo.service.ApiglRoleService;
import com.springsecuritydemo.service.ServiceUtil;
import com.springsecuritydemo.service.UserService;
import com.springsecuritydemo.util.ValidateUtil;

@DS("DBAPIGL")
@Service
public class ApiglRoleServiceImpl implements ApiglRoleService {

	@Autowired
	private ApiglRoleRepository apiglRoleRepository;
	
	@Autowired
	private ValidateUtil validateUtil;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ServiceUtil serviceUtil;
	
	/**
	 * ApiGLRole Table依查詢條件查詢,依分頁及排序Response
	 * @param ConditionsRequest
	 * @return ResponseEntity
	 * @throws ParseException 
	 */
    @Override
	public ResponseEntity<JSGridReturnData<ApiGLRole>> queryApiGLRole(ApiGLRole apiGLRole) throws ParseException {
    	checkData(apiGLRole);
		List<ApiGLRole> apiGLRoles = apiglRoleRepository.queryApiglRole(apiGLRole.getApiglRoleNumber(), apiGLRole.getApiglRoleName(), 
				apiGLRole.getApiglRoleType(), apiGLRole.getApiglRoleDirections(), apiGLRole.getEnabled(), 
				apiGLRole.getPageIndex(), apiGLRole.getPageSize());
		if(apiGLRoles.size() > 0 ) {
			//排序
			List<ApiGLRole> result = apiGLRoles.stream()
					.sorted(apiGLRoleSort(apiGLRole.getSortField(), apiGLRole.getSortOrder()))
					.collect(Collectors.toList());
		
			return new ResponseEntity<>(JSGridResponse.getResponseData(result, apiglRoleRepository.count()), HttpStatus.OK);
		} else {
			throw new QueryNoDataException("查無資料!!!", 404);
		}
	}

	/**
	 * 依ApiGLRole查詢
	 * @param apiglRolyId
	 * @return ApiGLRole
	 */
    @Override
	public ApiGLRole findByApiglRoleId(Long apiglRoleId) {
    	return serviceUtil.checkDataIsPresent(apiglRoleRepository.findById(apiglRoleId));
	}
	
    @Override
	public String save(ApiGLRole apiGLRole, String func, final Authentication authentication) throws JsonProcessingException {
    	if(func.equals("新增")) {
    		apiGLRole.setCreateTime(new Date());
    		apiGLRole.setCreateUser(userService.getSecurityUser(authentication).getAccount());
    	} else {
    		ApiGLRole oldApiGLRole = findByApiglRoleId(apiGLRole.getApiglRoleId());
    		apiGLRole.setCreateTime(oldApiGLRole.getCreateTime());
    		apiGLRole.setCreateUser(oldApiGLRole.getCreateUser());
    		apiGLRole.setUpdateTime(new Date());
    		apiGLRole.setUpdateUser(userService.getSecurityUser(authentication).getAccount());
    	}

    	if(validateUtil.isBlank(apiGLRole.getEnabled())) {
    		apiGLRole.setEnabled("0");
    	}
    	
    	ApiGLRole apiGLRoleResp = apiglRoleRepository.save(apiGLRole);
    	
    	if(validateUtil.isEmpty(apiGLRoleResp)) {
    		return func + "角色資料失敗";
    	}
    	
    	return func +"角色資料成功";
	}
    
    @Override
	public void delete(Long id) {
    	apiglRoleRepository.deleteById(id);
	}
    
	/**
	 * 排序欄位設置
	 * @param sortField
	 * @param sortOrder
	 * @return Comparator<ApiGLRole>
	 */
	private Comparator<ApiGLRole> apiGLRoleSort(String sortField, String sortOrder) {
		switch(sortField) {
		case "apiglRoleName":
			return sortOrder.equals("asc") ? 
					Comparator.comparing(ApiGLRole::getApiglRoleName) : 
						Comparator.comparing(ApiGLRole::getApiglRoleName).reversed();
		case "apiglRoleNumber":
			return sortOrder.equals("asc") ? 
					Comparator.comparing(ApiGLRole::getApiglRoleNumber) : 
						Comparator.comparing(ApiGLRole::getApiglRoleNumber).reversed();
		case "apiglRoleType":
			return sortOrder.equals("asc") ? 
					Comparator.comparing(ApiGLRole::getApiglRoleType) : 
						Comparator.comparing(ApiGLRole::getApiglRoleType).reversed();
		case "apiglRoleDirections":
			return sortOrder.equals("asc") ? 
					Comparator.comparing(ApiGLRole::getApiglRoleDirections) : 
						Comparator.comparing(ApiGLRole::getApiglRoleDirections).reversed();	
		default:
			return sortOrder.equals("asc") ? 
				Comparator.comparing(ApiGLRole::getApiglRoleId) : 
					Comparator.comparing(ApiGLRole::getApiglRoleId).reversed();
		}
	}
	
	/**
	 * 判斷是否有無資料,另外處理
	 * @param apiGLRole
	 */
	private void checkData(ApiGLRole apiGLRole) {
		if(validateUtil.isBlank(apiGLRole.getSortField())) {
			apiGLRole.setSortField("sort");
    	}
    	if(validateUtil.isBlank(apiGLRole.getSortOrder())) {
    		apiGLRole.setSortOrder("asc");
    	}
    	if(validateUtil.isNotBlank(apiGLRole.getApiglRoleDirections())) {
    		apiGLRole.setApiglRoleDirections("%" + apiGLRole.getApiglRoleDirections() + "%");
    	}
	}
}

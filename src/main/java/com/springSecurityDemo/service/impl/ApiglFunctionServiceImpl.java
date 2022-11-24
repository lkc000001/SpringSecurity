package com.springSecurityDemo.service.impl;

import java.text.ParseException;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.springSecurityDemo.entity.ApiGLFunction;
import com.springSecurityDemo.entity.AppUser;
import com.springSecurityDemo.entity.response.JSGridResponse;
import com.springSecurityDemo.entity.response.JSGridReturnData;
import com.springSecurityDemo.exception.QueryNoDataException;
import com.springSecurityDemo.repositories.ApiglFunctionRepository;
import com.springSecurityDemo.repositories.UserFunctionRepository;
import com.springSecurityDemo.service.ApiGLPermissionService;
import com.springSecurityDemo.service.ApiglFunctionService;
import com.springSecurityDemo.service.ServiceUtil;
import com.springSecurityDemo.service.UserService;
import com.springSecurityDemo.util.DateTimtUtil;
import com.springSecurityDemo.util.ValidateUtil;

@DS("DBAPIGL")
@Service
public class ApiglFunctionServiceImpl implements ApiglFunctionService {

	@Autowired
	ApiglFunctionRepository apiglFunctionRepository;
	
	@Autowired
	UserFunctionRepository userFunctionRepository;
	
	@Autowired
	ValidateUtil validateUtil;
	
	@Autowired
	DateTimtUtil dateTimtUtil;
	
	@Autowired
	ServiceUtil serviceUtil;
	
	@Autowired
	UserService userService;
	
	@Autowired
	ApiGLPermissionService apiGLPermissionService;
	
	/**
	 * ApiGLFunction Table依查詢條件查詢,依分頁及排序Response
	 * @param ConditionsRequest
	 * @return ResponseEntity
	 * @throws ParseException 
	 */
    @Override
	public ResponseEntity<?> queryApiglFunction(ApiGLFunction apiGLFunction) throws ParseException
	{
    	checkData(apiGLFunction);

		List<ApiGLFunction> apiGLFunctions = apiglFunctionRepository.queryApiglFunction(apiGLFunction.getApiglFunctionName(), apiGLFunction.getApiglFunctionShowName(), 
				apiGLFunction.getApiglFunctionUrl(), apiGLFunction.getEnabled(), apiGLFunction.getType());
		if(apiGLFunctions.size() > 0 ) {
		//分頁,排序
		int pageSize = apiGLFunction.getPageSize();
		List<ApiGLFunction> result = apiGLFunctions.stream()
			.sorted(apiGLFunctionSort(apiGLFunction.getSortField(), apiGLFunction.getSortOrder()))
			.skip(pageSize * (apiGLFunction.getPageIndex() - 1))
			.limit(pageSize)
			.collect(Collectors.toList());
		
			return new ResponseEntity<JSGridReturnData<ApiGLFunction>>(JSGridResponse.getResponseData(result, apiGLFunctions.size()), HttpStatus.OK);
		} else {
			throw new QueryNoDataException("查無資料!!!", 404);
		}
	}

    public List<ApiGLFunction> findAll() {
    	return apiglFunctionRepository.findAll();
    }
    
	/**
	 * 依apiglFunctionId查詢功能資料
	 * @param apiglFunctionId
	 * @return ApiGLFunction
	 */
    @Override
	public ApiGLFunction findByApiglFunctionId(Long apiglFunctionId) {
		return apiglFunctionRepository.findById(apiglFunctionId).get();
	}
	
    /**
     * 新增/修改
     * @param apiglFunctionId, func, session, request
	 * @return msg
     */
    @Override
	public String save(ApiGLFunction apiGLFunction, String func, final Authentication authentication) {
    	AppUser appUser = userService.getSecurityUser(authentication);
    	if(func.equals("新增")) {
    		apiGLFunction.setCreateTime(new Date());
    		apiGLFunction.setCreateUser(appUser.getAccount());
    	} else {
    		ApiGLFunction oldApiGLFunction = findByApiglFunctionId(apiGLFunction.getApiglFunctionId());
    		apiGLFunction.setCreateTime(oldApiGLFunction.getCreateTime());
    		apiGLFunction.setCreateUser(oldApiGLFunction.getCreateUser());
    		apiGLFunction.setUpdateTime(new Date());
    		apiGLFunction.setUpdateUser(appUser.getAccount());
    	}

    	if(validateUtil.isBlank(apiGLFunction.getEnabled())) {
    		apiGLFunction.setEnabled("0");
    	}

    	if(validateUtil.isIntegerNull(apiGLFunction.getApiglFunctionSort())) {
    		apiGLFunction.setApiglFunctionSort(0);
    	}
    	
    	ApiGLFunction apiGLFunctionResp = apiglFunctionRepository.save(apiGLFunction);
    	
    	if(validateUtil.isEmpty(apiGLFunctionResp)) {
    		return func + "功能資料失敗";
    	}

    	return func +"功能資料成功";
	}
    
    @Override
	public void delete(Long id) {
    	apiglFunctionRepository.deleteById(id);
	}
    
	/**
	 * 排序欄位設置
	 * @param sortField
	 * @param sortOrder
	 * @return Comparator<ApiGLFunction>
	 */
	private Comparator<ApiGLFunction> apiGLFunctionSort(String sortField, String sortOrder) {
		switch(sortField) {
		case "apiglFunctionId":
			return sortOrder.equals("asc") ? 
					Comparator.comparing(ApiGLFunction::getApiglFunctionId) : 
						Comparator.comparing(ApiGLFunction::getApiglFunctionId).reversed();
		case "apiglFunctionName":
			return sortOrder.equals("asc") ? 
					Comparator.comparing(ApiGLFunction::getApiglFunctionName) : 
						Comparator.comparing(ApiGLFunction::getApiglFunctionName).reversed();
		case "apiglFunctionShowName":
			return sortOrder.equals("asc") ? 
					Comparator.comparing(ApiGLFunction::getApiglFunctionShowName) : 
						Comparator.comparing(ApiGLFunction::getApiglFunctionShowName).reversed();
		case "apiglFunctionUrl":
			return sortOrder.equals("asc") ? 
					Comparator.comparing(ApiGLFunction::getApiglFunctionUrl) : 
						Comparator.comparing(ApiGLFunction::getApiglFunctionUrl).reversed();
		default:
			return sortOrder.equals("asc") ? 
				Comparator.comparing(ApiGLFunction::getApiglFunctionSort) : 
					Comparator.comparing(ApiGLFunction::getApiglFunctionSort).reversed();
		}
	}
	
	/**
	 * 確認欄位資料
	 * @param apiGLFunction
	 */
	private void checkData(ApiGLFunction apiGLFunction) {
		if(validateUtil.isBlank(apiGLFunction.getSortField())) {
    		apiGLFunction.setSortField("sort");
    	}
    	if(validateUtil.isBlank(apiGLFunction.getSortOrder())) {
    		apiGLFunction.setSortOrder("asc");
    	}
    	if(validateUtil.isNotBlank(apiGLFunction.getApiglFunctionName())) {
    		apiGLFunction.setApiglFunctionName("%" + apiGLFunction.getApiglFunctionName() + "%");
    	}
    	if(validateUtil.isNotBlank(apiGLFunction.getApiglFunctionShowName())) {
    		apiGLFunction.setApiglFunctionShowName("%" + apiGLFunction.getApiglFunctionShowName() + "%");
    	}
    	if(validateUtil.isNotBlank(apiGLFunction.getApiglFunctionUrl())) {
    		apiGLFunction.setApiglFunctionUrl("%" + apiGLFunction.getApiglFunctionUrl() + "%");
    	}
	}
    
}

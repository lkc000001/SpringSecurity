package com.springsecuritydemo.service.impl;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.springsecuritydemo.entity.ApiGLFunction;
import com.springsecuritydemo.entity.ApiGLRole;
import com.springsecuritydemo.entity.AppUser;
import com.springsecuritydemo.repositories.ApiglFunctionRepository;
import com.springsecuritydemo.repositories.ApiglRoleRepository;
import com.springsecuritydemo.repositories.UserRepository;
import com.springsecuritydemo.service.SystemSetupService;
import com.springsecuritydemo.util.ExcelUtil;
import com.springsecuritydemo.util.ValidateUtil;

@DS("DBAPIGL")
@Service
public class SystemSetupServiceImpl implements SystemSetupService {

	@Autowired
	private ApiglRoleRepository apiglRoleRepository;
	
	@Autowired
	private ApiglFunctionRepository apiglFunctionRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ValidateUtil validateUtil;
	
	@Autowired
	private ExcelUtil excelUtil;
	
	private static final String UPDATA_STRING = "上傳";
	
	private static final String PASS_STRING = "筆資料成功";
	
    @Override
    public String apiGLRoleUpload(MultipartFile file, String userName) throws EncryptedDocumentException, IOException {
		Workbook wb = WorkbookFactory.create(file.getInputStream());
		Object obj = excelUtil.parseExcel(wb, 1);
		List<ApiGLRole> apiGLRoles = (List<ApiGLRole>)obj;
		int count = 0;
		for(ApiGLRole role: apiGLRoles) {
			role.setCreateUser(userName);
			if (validateUtil.isBlank(role.getApiglRoleNumber())) {
				role.setApiglRoleNumber("");
			}
			
			ApiGLRole apiGLRole = apiglRoleRepository.findByApiglRoleNameAndApiglRoleNumberAndApiglRoleType(role.getApiglRoleName(), role.getApiglRoleNumber(), role.getApiglRoleType());
			if(apiGLRole != null) {
				role.setApiglRoleId(apiGLRole.getApiglRoleId());
				role.setCreateTime(apiGLRole.getCreateTime());
				role.setCreateUser(apiGLRole.getCreateUser());
				role.setUpdateTime(new Date());
				role.setUpdateUser(userName);
			}
			if(validateUtil.isNotBlank(role.getApiglRoleName())) {
				apiglRoleRepository.save(role);
				count ++;
			}
		}
    	return UPDATA_STRING + count + PASS_STRING;
    }
    
    @Override
    public String apiglFunctionUpload(MultipartFile file, String userName) throws EncryptedDocumentException, IOException {
		Workbook wb = WorkbookFactory.create(file.getInputStream());
		Object obj = excelUtil.parseExcel(wb, 2);
		List<ApiGLFunction> apiGLFunctions = (List<ApiGLFunction>)obj;
		int count = 0;
		for(ApiGLFunction function: apiGLFunctions) {
			function.setCreateUser(userName);
			ApiGLFunction apiGLFunction = apiglFunctionRepository.findByApiglFunctionName(function.getApiglFunctionName());
				if(apiGLFunction != null) {
					function.setApiglFunctionId(apiGLFunction.getApiglFunctionId());
					function.setCreateTime(apiGLFunction.getCreateTime());
					function.setCreateUser(apiGLFunction.getCreateUser());
					function.setUpdateTime(new Date());
					function.setUpdateUser(userName);
				}
				
				if(validateUtil.isNotBlank(function.getApiglFunctionName())) {
					apiglFunctionRepository.save(function);
					count ++;
				}
			}
    	return UPDATA_STRING + count + PASS_STRING;
    }
    
    @Override
    public String userUpload(MultipartFile file, String userName) throws EncryptedDocumentException, IOException {
		Workbook wb = WorkbookFactory.create(file.getInputStream());
		Object obj = excelUtil.parseExcel(wb, 3);
		List<AppUser> users = (List<AppUser>)obj;
		int count = 0;
		for(AppUser user: users) {	
			user.setCreateUser(userName);
			AppUser oldUser = userRepository.findByAccount(user.getAccount());
			if(oldUser != null) {
				user.setUserId(oldUser.getUserId());
				user.setPwd(oldUser.getPwd());
				user.setCreateTime(oldUser.getCreateTime());
				user.setCreateUser(oldUser.getCreateUser());
				user.setUpdateTime(new Date());
				user.setUpdateUser(userName);
			}
			
			if(validateUtil.isNotBlank(user.getAccount())) {
				userRepository.save(user);
				count ++;
			}
		}
    	return UPDATA_STRING + count + PASS_STRING;
    }
}

package com.springSecurityDemo.controller;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.springSecurityDemo.entity.ApiGLRole;
import com.springSecurityDemo.service.ApiglRoleService;
import com.springSecurityDemo.util.ValidateUtil;
import com.fasterxml.jackson.core.JsonProcessingException;

@Controller
@RequestMapping(value = "/apiGLRole")
public class ApiGLRoleController {
	
	@Autowired
	ApiglRoleService apiglRoleService;
	
	@Autowired
	ValidateUtil validateUtil;
	
    @GetMapping("/")
    public String index(Model model) {
    	model.addAttribute("selectFunction", "ApiglRole");
    	return "apiGLRole";
    }
    
    /**
	 * 依查詢條件查詢TWPayLineBindLog Table資料
	 * @param session
	 * @param ConditionsRequest
	 * @return 排序及分頁後的ListTWPayLineBindLog
	 * @throws ParseException 
	 */
	@PostMapping(path = "/", consumes = "application/json", produces = "application/json")
	@ResponseBody
	public ResponseEntity<?> queryApiGLRole(@RequestBody ApiGLRole apiglRole) throws ParseException {
		return apiglRoleService.queryApiGLRole(apiglRole);
    }
   
	/**
	 * 依apiglRoleId查詢ApiGLRole Table資料
	 * @param apiglRoleId
	 * @return ApiGLRole
	 */
	@GetMapping(path = "/get/{apiglRoleId}")
	@ResponseBody
	public ApiGLRole findByApiglRoleId(@PathVariable("apiglRoleId") Long apiglRoleId) {
		return apiglRoleService.findByApiglRoleId(apiglRoleId);
    }
	
    /**
	 * 新增
	 * @param apiGLFunction
	 * @return
	 * @throws JsonProcessingException
	 */
	@PostMapping(path = "/save", consumes = "application/json", produces = "application/json")
	@ResponseBody
	public ResponseEntity<?>  add(final Authentication authentication, @RequestBody ApiGLRole apiglRole) throws JsonProcessingException {
    	String respMsg = apiglRoleService.save(apiglRole, "新增", authentication);
    	return new ResponseEntity<>("{\"message\": \"" + respMsg + "\"}",HttpStatus.OK);
    }
    
    /**
     * 修改
     * @param apiGLFunction
     * @return
     * @throws JsonProcessingException
     */
	@PutMapping(path = "/save", consumes = "application/json", produces = "application/json")
    @ResponseBody
	public ResponseEntity<?>  update(final Authentication authentication, @RequestBody ApiGLRole apiglRole) throws JsonProcessingException {
    	String respMsg = apiglRoleService.save(apiglRole, "修改", authentication);
    	return new ResponseEntity<>("{\"message\": \"" + respMsg + "\"}",HttpStatus.OK);
    }
    
    /**
     * 刪除
     * @param id
     * @return
     */
    @DeleteMapping(path = "/delete", consumes = "application/json", produces = "application/json")
    @ResponseBody
	public ResponseEntity<?> delete(@RequestBody ApiGLRole apiglRole){
    	apiglRoleService.delete(apiglRole.getApiglRoleId());
		return new ResponseEntity<>("{\"message\": \"刪除成功\"}",HttpStatus.OK);
    }
}

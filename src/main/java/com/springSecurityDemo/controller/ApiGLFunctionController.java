package com.springsecuritydemo.controller;

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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.springsecuritydemo.entity.ApiGLFunction;
import com.springsecuritydemo.entity.response.JSGridReturnData;
import com.springsecuritydemo.service.ApiglFunctionService;
import com.springsecuritydemo.util.ValidateUtil;

@Controller
@RequestMapping(value = "/apiGLFunction")
public class ApiGLFunctionController {
	
	@Autowired
	ApiglFunctionService apiglFunctionService;
	
	@Autowired
	ValidateUtil validateUtil;
	
	private String respMsg;
	
    @GetMapping("/")
    public String index(Model model) {
    	if (validateUtil.isNotBlank(respMsg)) {
    		model.addAttribute("apiglfunctionmsg", respMsg);
    		respMsg = null;
    	}
    	model.addAttribute("selectFunction", "ApiglFunction");
    	return "apiGLFunction";
    }
    
    /**
	 * 依查詢條件查詢ApiGLFunction Table資料
	 * @param session
	 * @param ApiGLFunction
	 * @return 排序及分頁後的ApiGLFunction
	 * @throws ParseException 
	 */
	@PostMapping(path = "/", consumes = "application/json", produces = "application/json")
	public ResponseEntity<JSGridReturnData<ApiGLFunction>> queryApiglFunction(@RequestBody ApiGLFunction apiGLFunction) throws ParseException {
		return apiglFunctionService.queryApiglFunction(apiGLFunction);
    }
	
	/**
	 * 依apiglRoleId查詢ApiGLRole Table資料
	 * @param apiglRoleId
	 * @return ApiGLRole
	 */
	@GetMapping(path = "/get/{apiglFunctionId}")
	@ResponseBody
	public ApiGLFunction getApiGLFunction(@PathVariable("apiglFunctionId") Long apiglFunctionId) {
		return apiglFunctionService.findByApiglFunctionId(apiglFunctionId);
    }
	
	/**
	 * 新增
	 * @param apiGLFunction
	 * @return
	 * @throws JsonProcessingException
	 */
    @PostMapping(path = "/save")
	public String add(final Authentication authentication, ApiGLFunction apiGLFunction) {
    	respMsg = apiglFunctionService.save(apiGLFunction, "新增", authentication);
		return "redirect:./";
    }
    
    /**
     * 修改
     * @param apiGLFunction
     * @return
     * @throws JsonProcessingException
     */
    @PutMapping(path = "/save")
	public String update(final Authentication authentication, ApiGLFunction apiGLFunction) {
    	respMsg = apiglFunctionService.save(apiGLFunction, "修改", authentication);
		return "redirect:./";
    }
    
    /**
     * 刪除
     * @param id
     * @return
     */
    @DeleteMapping(path = "/delete")
	public ResponseEntity<String> delete(Long id){
    	apiglFunctionService.delete(id);
		return new ResponseEntity<>(HttpStatus.OK);
    }
}

package com.springsecuritydemo.controller;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.springsecuritydemo.entity.AppUser;
import com.springsecuritydemo.entity.response.JSGridReturnData;
import com.springsecuritydemo.service.UserService;
import com.springsecuritydemo.util.ValidateUtil;

@Controller
@RequestMapping(value = "/userPermission")
public class UserPermissionController {
	
	@Autowired
	UserService userService;
	
	@Autowired
	ValidateUtil validateUtil;
	
	@GetMapping("/")
    public String index(Model model) {
		model.addAttribute("selectFunction", "UserPermission");
    	return "userPermission";
    }
	
	/**
	 * 依查詢條件查詢ApiGLFunction Table資料
	 * @param session
	 * @param ApiGLFunction
	 * @return 排序及分頁後的ApiGLFunction
	 * @throws ParseException 
	 */
	@PostMapping(path = "/", consumes = "application/json", produces = "application/json")
	public ResponseEntity<JSGridReturnData<AppUser>> queryUserPermission(@RequestBody AppUser user) throws ParseException {
		return userService.queryUserPermission(user);
    }

	@GetMapping(path = "/get/{userId}")
	@ResponseBody
	public AppUser getUser(@PathVariable("userId") Long userId) {
		return userService.findByUserId(userId);
	}
	
	/**
	 * 新增
	 * @param apiGLFunction
	 * @return
	 * @throws JsonProcessingException
	 */
    @PostMapping(path = "/save", consumes = "application/json", produces = "application/json")
	@ResponseBody
    public ResponseEntity<String> add(final Authentication authentication, @RequestBody AppUser user) {
    	String respMsg = userService.save(user, "新增", authentication);
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
    public ResponseEntity<String> update(final Authentication authentication, @RequestBody AppUser user) {
    	String respMsg = userService.save(user, "修改", authentication);
    	return new ResponseEntity<>("{\"message\": \"" + respMsg + "\"}",HttpStatus.OK);
    }
    
}

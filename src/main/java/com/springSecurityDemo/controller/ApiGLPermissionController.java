package com.springsecuritydemo.controller;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.springsecuritydemo.entity.ApiGLRole;
import com.springsecuritydemo.entity.AppUser;
import com.springsecuritydemo.entity.PermissionResponse;
import com.springsecuritydemo.entity.RoleFunction;
import com.springsecuritydemo.entity.UserFunction;
import com.springsecuritydemo.service.ApiGLPermissionService;
import com.springsecuritydemo.service.ApiglRoleService;
import com.springsecuritydemo.service.UserService;
import com.springsecuritydemo.util.ValidateUtil;

@Controller
@RequestMapping(value = "/apiGLPermission")
public class ApiGLPermissionController {
	
	@Autowired
	ApiglRoleService apiglRoleService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	ApiGLPermissionService apiGLPermissionService;
	
	@Autowired
	ValidateUtil validateUtil;
	
	@GetMapping("/")
    public String index(Model model) {
		model.addAttribute("selectFunction", "UserPermission");
		//model.addAttribute("apiglrole", new ApiGLRole());
		//model.addAttribute("user", new AppUser());
    	return "apiGLPermission";
    }
	
    @GetMapping("/role/{apiglRoleId}")
    public String role(Model model, @PathVariable("apiglRoleId") Long apiglRoleId) {
    	Set<PermissionResponse> rolePermission = apiGLPermissionService.queryRolePermission(apiglRoleId);
    	model.addAttribute("selectFunction", "ApiglRole");
    	model.addAttribute("apiglrole", apiglRoleService.findByApiglRoleId(apiglRoleId));
    	model.addAttribute("user", new AppUser());
    	model.addAttribute("funcname", "role");
    	model.addAttribute("permission", createFunctionsTableStr(rolePermission));
    	return "apiGLPermission";
    }

    @PostMapping(path = "/role/update", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public ResponseEntity<String> roleupdate(final Authentication authentication, Model model, @RequestBody Set<RoleFunction> roleFunctions) {
    	apiGLPermissionService.saveRoleFunctions(roleFunctions, authentication);
    	return new ResponseEntity<>("{\"message\": \"修改成功\"}",HttpStatus.OK);
    }
    
    @GetMapping("/user/{userId}")
    public String user(Model model, @PathVariable("userId") Long userId) {
    	Set<PermissionResponse> userPermission = apiGLPermissionService.queryUserPermission(userId);
    	//System.out.println(userService.findByUserId(userId));
    	model.addAttribute("selectFunction", "UserPermission");
    	model.addAttribute("user", userService.findByUserId(userId));
    	model.addAttribute("apiglrole", new ApiGLRole());
    	model.addAttribute("funcname", "user");
    	model.addAttribute("permission", createFunctionsTableStr(userPermission));
    	return "apiGLPermission";
    }
    
    @PostMapping(path = "/user/update", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public ResponseEntity<String> userupdate(final Authentication authentication, Model model, @RequestBody Set<UserFunction> userFunctions) {
    	apiGLPermissionService.saveUserFunctions(userFunctions, authentication);
    	return new ResponseEntity<>("{\"message\": \"修改成功\"}",HttpStatus.OK);
    }
    
    private String isEnabled(String permissionEnabled) {
    	String respStr = "";
    	if(validateUtil.isBlank(permissionEnabled) || permissionEnabled.equals("0")) {
    		respStr += "<td><input type=\"checkbox\" value=\"1\"></td>";
		} else {
			respStr += "<td><input type=\"checkbox\" value=\"1\" checked=\"checked\"></td>";
		}
    	return respStr;
    } 
    
    private String createFunctionsTableStr(Set<PermissionResponse> rolePermission) {
    	String tr = "<tr>";
    	String td = "<td>";
    	String trEnd = "<tr>";
    	String tdEnd = "</td>";
    	StringBuilder functionsTable = new StringBuilder(tr);
    	int index = 0;
    	for(PermissionResponse perm: rolePermission) {
    		if(index == 2) {
    			functionsTable.append(trEnd);
    			functionsTable.append(tr + td);
    			functionsTable.append(perm.getApiglfunctionid());
    			functionsTable.append(tdEnd + td);
    			functionsTable.append(perm.getApiglfunctionshowname());
    			functionsTable.append(tdEnd);
    			functionsTable.append(isEnabled(perm.getPermissionenabled()));
    			index = 0;
    		} else {
    			functionsTable.append(td);
    			functionsTable.append(perm.getApiglfunctionid());
    			functionsTable.append(tdEnd + td);
    			functionsTable.append(perm.getApiglfunctionshowname());
    			functionsTable.append(tdEnd);
    			functionsTable.append(isEnabled(perm.getPermissionenabled()));
    		}
    		index +=1;
    	}
    	functionsTable.append(trEnd);
    	return functionsTable.toString();
    }
    
}

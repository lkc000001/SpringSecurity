package com.springSecurityDemo.controller;

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

import com.springSecurityDemo.entity.ApiGLRole;
import com.springSecurityDemo.entity.AppUser;
import com.springSecurityDemo.entity.RoleFunction;
import com.springSecurityDemo.entity.UserFunction;
import com.springSecurityDemo.entity.PermissionResponse;
import com.springSecurityDemo.service.ApiglRoleService;
import com.springSecurityDemo.service.UserService;
import com.springSecurityDemo.service.ApiGLPermissionService;
import com.springSecurityDemo.util.ValidateUtil;

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
    	Set<PermissionResponse> RolePermission = apiGLPermissionService.queryRolePermission(apiglRoleId);
    	model.addAttribute("selectFunction", "ApiglRole");
    	model.addAttribute("apiglrole", apiglRoleService.findByApiglRoleId(apiglRoleId));
    	model.addAttribute("user", new AppUser());
    	model.addAttribute("funcname", "role");
    	model.addAttribute("permission", createFunctionsTableStr(RolePermission));
    	return "apiGLPermission";
    }

    @PostMapping(path = "/role/update", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public ResponseEntity<?> roleupdate(final Authentication authentication, Model model, @RequestBody Set<RoleFunction> roleFunctions) {
    	apiGLPermissionService.saveRoleFunctions(roleFunctions, authentication);
    	return new ResponseEntity<>("{\"message\": \"修改成功\"}",HttpStatus.OK);
    }
    
    @GetMapping("/user/{userId}")
    public String user(Model model, @PathVariable("userId") Long userId) {
    	Set<PermissionResponse> UserPermission = apiGLPermissionService.queryUserPermission(userId);
    	//System.out.println(userService.findByUserId(userId));
    	model.addAttribute("selectFunction", "UserPermission");
    	model.addAttribute("user", userService.findByUserId(userId));
    	model.addAttribute("apiglrole", new ApiGLRole());
    	model.addAttribute("funcname", "user");
    	model.addAttribute("permission", createFunctionsTableStr(UserPermission));
    	return "apiGLPermission";
    }
    
    @PostMapping(path = "/user/update", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public ResponseEntity<?> userupdate(final Authentication authentication, Model model, @RequestBody Set<UserFunction> userFunctions) {
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
    
    private String createFunctionsTableStr(Set<PermissionResponse> RolePermission) {
    	StringBuilder functionsTable = new StringBuilder("<tr>");
    	int index = 0;
    	for(PermissionResponse perm: RolePermission) {
    		if(index == 2) {
    			functionsTable.append("</tr>");
    			functionsTable.append("<tr><td>");
    			functionsTable.append(perm.getApiglfunctionid());
    			functionsTable.append("</td><td>");
    			functionsTable.append(perm.getApiglfunctionshowname());
    			functionsTable.append ("</td>");
    			functionsTable.append(isEnabled(perm.getPermissionenabled()));
    			index = 0;
    		} else {
    			functionsTable.append("<td>");
    			functionsTable.append(perm.getApiglfunctionid());
    			functionsTable.append("</td><td>");
    			functionsTable.append(perm.getApiglfunctionshowname());
    			functionsTable.append ("</td>");
    			functionsTable.append(isEnabled(perm.getPermissionenabled()));
    		}
    		index +=1;
    	}
    	functionsTable.append("</tr>");
    	return functionsTable.toString();
    }
    
}

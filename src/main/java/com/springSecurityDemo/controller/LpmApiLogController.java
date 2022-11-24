package com.springSecurityDemo.controller;

import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.springSecurityDemo.entity.AppUser;
import com.springSecurityDemo.entity.LpmApiLog;
import com.springSecurityDemo.entity.request.ConditionsRequest;
import com.springSecurityDemo.entity.response.JSGridResponse;
import com.springSecurityDemo.entity.response.JSGridReturnData;
import com.springSecurityDemo.service.LpmApiLogService;
import com.springSecurityDemo.service.UserService;

@Controller
@RequestMapping(value = "/lpmApiLog")
public class LpmApiLogController {
	
	@Autowired
	LpmApiLogService lpmApiLogService;
	
	@Autowired
	UserService userService;
	
	@GetMapping("/")
    public String index(Model model) {
		model.addAttribute("selectFunction", "LpmApiLog");
    	return "lpmApiLog";
    }
	
	/**
	 * 依查詢條件查詢LpmApiLog Table資料
	 * @param session
	 * @param conditionsRequest
	 * @return 排序及分頁後的List<LpmApiLog>
	 * @throws ParseException 
	 */
	@PostMapping(path = "/", consumes = "application/json", produces = "application/json")
	@ResponseBody
	public ResponseEntity<?> queryLpmApiLog(@RequestBody ConditionsRequest conditionsRequest) throws ParseException {
		return lpmApiLogService.queryLpmApiLog(conditionsRequest);
    }	
}

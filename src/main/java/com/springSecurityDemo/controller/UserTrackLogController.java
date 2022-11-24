package com.springSecurityDemo.controller;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.springSecurityDemo.entity.request.ConditionsRequest;
import com.springSecurityDemo.service.UserTrackLogService;

@Controller
@RequestMapping(value = "/userTrackLog")
public class UserTrackLogController {
	
	@Autowired
	UserTrackLogService userTrackLogService;
	
	@GetMapping("/")
    public String index(Model model) {
		model.addAttribute("selectFunction", "UserTrackLog");
    	return "userTrackLog";
    }
	
	/**
	 * 依查詢條件查詢UserTrackLog Table資料
	 * @param ConditionsRequest
	 * @return 排序及分頁後的UserTrackLog
	 * @throws ParseException 
	 */
	@PostMapping(path = "/", consumes = "application/json", produces = "application/json")
	public ResponseEntity<?> queryUserTrackLog(@RequestBody ConditionsRequest conditionsRequest) throws ParseException {
		return userTrackLogService.queryUserTrackLog(conditionsRequest);
    }
	

}

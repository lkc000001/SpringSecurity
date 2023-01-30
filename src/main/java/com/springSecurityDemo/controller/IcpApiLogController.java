package com.springsecuritydemo.controller;

import java.text.ParseException;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.springsecuritydemo.entity.IcpApiLog;
import com.springsecuritydemo.entity.request.ConditionsRequest;
import com.springsecuritydemo.entity.response.JSGridReturnData;
import com.springsecuritydemo.service.IcpApiLogService;

@Controller
@RequestMapping(value = "/icpApiLog")
public class IcpApiLogController {
	
	@Autowired
	IcpApiLogService icpApiLogService;
	
	@GetMapping("/")
    public String index(Model model) {
		model.addAttribute("selectFunction", "IcpApiLog");
    	return "icpApiLog";
    }
	
	/**
	 * 依查詢條件查詢IcpApiLog Table資料
	 * @param session
	 * @param ConditionsRequest
	 * @return 排序及分頁後的IcpApiLog List 
	 * @throws ParseException 
	 */
	@PostMapping(path = "/", consumes = "application/json", produces = "application/json")
	public ResponseEntity<JSGridReturnData<IcpApiLog>> queryIcpApiLog(HttpSession session, @RequestBody ConditionsRequest conditionsRequest) throws ParseException {
		return icpApiLogService.queryIcpApiLog(conditionsRequest);
    }
}

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
import org.springframework.web.bind.annotation.ResponseBody;

import com.springsecuritydemo.entity.TwPayLineBindLog;
import com.springsecuritydemo.entity.request.ConditionsRequest;
import com.springsecuritydemo.entity.response.JSGridReturnData;
import com.springsecuritydemo.service.TwPayLineBindLogService;

@Controller
@RequestMapping(value = "/twPayLineBindLog")
public class TwPayLineBindLogController {
	
	@Autowired
	TwPayLineBindLogService twPayLineBindLogService;
	
	@GetMapping("/")
    public String index(Model model) {
		model.addAttribute("selectFunction", "TwPayLineBindLog");
    	return "twPayLineBindLog";
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
	public ResponseEntity<JSGridReturnData<TwPayLineBindLog>> queryTwPayLineBindLog(HttpSession session, @RequestBody ConditionsRequest conditionsRequest) throws ParseException {
		return twPayLineBindLogService.querytwPayLineBindLog(conditionsRequest);
    }
	

}

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

import com.springsecuritydemo.entity.Hpg5000ApiLog;
import com.springsecuritydemo.entity.request.ConditionsRequest;
import com.springsecuritydemo.entity.response.JSGridReturnData;
import com.springsecuritydemo.service.Hpg5000ApiLogService;

@Controller
@RequestMapping(value = "/hpg5000ApiLog")
public class Hpg5000ApiLogController {
	
	@Autowired
	Hpg5000ApiLogService hpg5000ApiLogService;
	
	@GetMapping("/")
    public String index(Model model) {
		model.addAttribute("selectFunction", "Hpg5000ApiLog");
    	return "hpg5000ApiLog";
    }
	
	/**
	 * 依查詢條件查詢Hpg5000ApiLog Table資料
	 * @param session
	 * @param ConditionsRequest
	 * @return 排序及分頁後的Hpg5000ApiLog List
	 * @throws ParseException 
	 */
	@PostMapping(path = "/", consumes = "application/json", produces = "application/json")
	public ResponseEntity<JSGridReturnData<Hpg5000ApiLog>> queryHpg5000ApiLog(HttpSession session, @RequestBody ConditionsRequest conditionsRequest) throws ParseException {
		return hpg5000ApiLogService.queryHpg5000ApiLog(conditionsRequest);
    }
}

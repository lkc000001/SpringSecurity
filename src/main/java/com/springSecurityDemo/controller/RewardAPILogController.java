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

import com.springsecuritydemo.entity.RewardAPILog;
import com.springsecuritydemo.entity.request.ConditionsRequest;
import com.springsecuritydemo.entity.response.JSGridReturnData;
import com.springsecuritydemo.service.RewardApiLogService;

@Controller
@RequestMapping(value = "/rewardApiLog")
public class RewardAPILogController {
	
	@Autowired
	RewardApiLogService rewardAPILogService;
	
	@GetMapping("/")
    public String index(Model model) {
		model.addAttribute("selectFunction", "RewardApiLog");
    	return "rewardApiLog";
    }
	
	/**
	 * 依查詢條件查詢RewardAPILog Table資料
	 * @param session
	 * @param ConditionsRequest
	 * @return 排序及分頁後的List RewardAPILog
	 * @throws ParseException 
	 */
	@PostMapping(path = "/", consumes = "application/json", produces = "application/json")
	public ResponseEntity<JSGridReturnData<RewardAPILog>> queryRewardApiLog(HttpSession session, @RequestBody ConditionsRequest conditionsRequest) throws ParseException {
		return rewardAPILogService.queryRewardAPILog(conditionsRequest);
    }
}

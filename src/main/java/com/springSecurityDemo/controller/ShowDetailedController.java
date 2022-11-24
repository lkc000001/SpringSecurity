package com.springSecurityDemo.controller;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springSecurityDemo.entity.request.ConditionsRequest;
import com.springSecurityDemo.service.ApiGuiLogService;
import com.springSecurityDemo.service.Hpg5000ApiLogService;
import com.springSecurityDemo.service.IcpApiLogService;
import com.springSecurityDemo.service.LpmApiLogService;
import com.springSecurityDemo.service.MtpApiLogService;
import com.springSecurityDemo.service.RewardApiLogService;
import com.springSecurityDemo.service.SmsOtpLogService;
import com.springSecurityDemo.service.TwPayLineBindLogService;
import com.springSecurityDemo.service.UserTrackLogService;

@Controller
@RequestMapping(value = "/showDetailed")
public class ShowDetailedController {
	
	@Autowired
	LpmApiLogService lpmApiLogService;
	
	@Autowired
	TwPayLineBindLogService twPayLineBindLogService;
	
	@Autowired
	RewardApiLogService rewardAPILogService;
	
	@Autowired
	IcpApiLogService icpAPILogService;
	
	@Autowired
	MtpApiLogService mtpAPILogService;
	
	@Autowired
	Hpg5000ApiLogService hpg5000ApiLogService;
	
	@Autowired
	SmsOtpLogService smsOtpLogService;
	
	@Autowired
	ApiGuiLogService apiGuiLogService;
	
	@Autowired
	UserTrackLogService userTrackLogService;
	
	@Autowired
	ObjectMapper objectMapper;
	
	/**
	 * 依table查詢祥細資料
	 * @param ConditionsRequest
	 * @param model
	 * @return ModelAndView(回傳查詢資料及轉導到showDetailed.jsp)
	 */
	@PostMapping(path = "/")
	public String doShowDetailed(Model model, ConditionsRequest conditionsRequest) {
		String queryTable =  conditionsRequest.getQueryTable();
		Long queryId = conditionsRequest.getQueryId();
		Object respTable = null;
		try {
			
			switch(queryTable) {
			case "LpmApiLog":
				respTable = lpmApiLogService.findByLpmid(queryId);
				break;
			case "TwPayLineBindLog":
				respTable = twPayLineBindLogService.findBytwPayLineBindLogId(queryId);
				break;
			case "RewardApiLog":
				respTable = rewardAPILogService.findByrewardId(queryId);
				break;
			case "IcpApiLog":
				respTable = icpAPILogService.findByicpId(queryId);
				break;
			case "MtpApiLog":
				respTable = mtpAPILogService.findBymtpLId(queryId);
				break;
			case "Hpg5000ApiLog":
				respTable = hpg5000ApiLogService.findByhpgLogId(queryId);
				break;
			case "SmsOtpLog":
				respTable = smsOtpLogService.findBySolId(queryId);
				break;
			case "ApiGuiLog":
				respTable = apiGuiLogService.findByApiGuiId(queryId);
				break;
			case "UserTrackLog":
				respTable = userTrackLogService.findByUserTrackId(queryId);
				break;	
			}

			model.addAttribute("selectFunction", queryTable);
			model.addAttribute("detailedresp", objectMapper.writeValueAsString(respTable));
			//throw new Exception();
			return "showDetailed";
		} catch (JsonProcessingException e) {
			model.addAttribute("errorMsg", "Json格式轉換錯誤!!! <BR>");
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMsg", "查詢失敗!!! <BR>");
		}
		return "error";
    }
}

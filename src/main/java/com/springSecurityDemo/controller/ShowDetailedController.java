package com.springsecuritydemo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springsecuritydemo.entity.request.ConditionsRequest;
import com.springsecuritydemo.exception.QueryNoDataException;
import com.springsecuritydemo.service.ApiGuiLogService;
import com.springsecuritydemo.service.Hpg5000ApiLogService;
import com.springsecuritydemo.service.IcpApiLogService;
import com.springsecuritydemo.service.LpmApiLogService;
import com.springsecuritydemo.service.MtpApiLogService;
import com.springsecuritydemo.service.RewardApiLogService;
import com.springsecuritydemo.service.SmsOtpLogService;
import com.springsecuritydemo.service.TwPayLineBindLogService;
import com.springsecuritydemo.service.UserTrackLogService;

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
			default: 
				throw new QueryNoDataException("查詢失敗!!! <BR>", 400);
			}

			model.addAttribute("selectFunction", queryTable);
			model.addAttribute("detailedresp", objectMapper.writeValueAsString(respTable));
			return "showDetailed";
		} catch (JsonProcessingException e) {
			model.addAttribute("errorMsg", "Json格式轉換錯誤!!! <BR>");
		} catch (Exception e) {
			model.addAttribute("errorMsg", "查詢失敗!!! <BR>");
		}
		return "error";
    }
}

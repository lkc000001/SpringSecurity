package com.springsecuritydemo.service;

import java.text.ParseException;

import org.springframework.http.ResponseEntity;

import com.springsecuritydemo.entity.RewardAPILog;
import com.springsecuritydemo.entity.request.ConditionsRequest;
import com.springsecuritydemo.entity.response.JSGridReturnData;


public interface RewardApiLogService {
	
	public ResponseEntity<JSGridReturnData<RewardAPILog>> queryRewardAPILog(ConditionsRequest conditionsRequest) throws ParseException;

	public RewardAPILog findByrewardId(Long rewardId);
}

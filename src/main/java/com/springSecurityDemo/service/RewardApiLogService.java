package com.springSecurityDemo.service;

import java.text.ParseException;

import org.springframework.http.ResponseEntity;

import com.springSecurityDemo.entity.RewardAPILog;
import com.springSecurityDemo.entity.request.ConditionsRequest;


public interface RewardApiLogService {
	
	public ResponseEntity<?> queryRewardAPILog(ConditionsRequest conditionsRequest) throws ParseException;

	public RewardAPILog findByrewardId(Long rewardId);
}

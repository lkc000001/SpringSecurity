package com.springsecuritydemo.service;

import java.text.ParseException;

import org.springframework.http.ResponseEntity;

import com.springsecuritydemo.entity.SmsOtpLog;
import com.springsecuritydemo.entity.request.ConditionsRequest;
import com.springsecuritydemo.entity.response.JSGridReturnData;

public interface SmsOtpLogService {
	
	public ResponseEntity<JSGridReturnData<SmsOtpLog>> querySmsOtpLog(ConditionsRequest conditionsRequest) throws ParseException;

	public SmsOtpLog findBySolId(Long solId);
}

package com.springSecurityDemo.service;

import java.text.ParseException;

import org.springframework.http.ResponseEntity;

import com.springSecurityDemo.entity.SmsOtpLog;
import com.springSecurityDemo.entity.request.ConditionsRequest;

public interface SmsOtpLogService {
	
	public ResponseEntity<?> querySmsOtpLog(ConditionsRequest conditionsRequest) throws ParseException;

	public SmsOtpLog findBySolId(Long solId);
}

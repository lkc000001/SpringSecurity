package com.springSecurityDemo.service;

import java.text.ParseException;

import org.springframework.http.ResponseEntity;

import com.springSecurityDemo.entity.MtpApiLog;
import com.springSecurityDemo.entity.request.ConditionsRequest;

public interface MtpApiLogService {
	
	public ResponseEntity<?> queryMtpApiLog(ConditionsRequest conditionsRequest) throws ParseException;

	public MtpApiLog findBymtpLId(Long mtpLId);
}

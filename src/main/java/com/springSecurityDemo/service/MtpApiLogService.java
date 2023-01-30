package com.springsecuritydemo.service;

import java.text.ParseException;

import org.springframework.http.ResponseEntity;

import com.springsecuritydemo.entity.MtpApiLog;
import com.springsecuritydemo.entity.request.ConditionsRequest;
import com.springsecuritydemo.entity.response.JSGridReturnData;

public interface MtpApiLogService {
	
	public ResponseEntity<JSGridReturnData<MtpApiLog>> queryMtpApiLog(ConditionsRequest conditionsRequest) throws ParseException;

	public MtpApiLog findBymtpLId(Long mtpLId);
}

package com.springsecuritydemo.service;

import java.text.ParseException;

import org.springframework.http.ResponseEntity;

import com.springsecuritydemo.entity.LpmApiLog;
import com.springsecuritydemo.entity.request.ConditionsRequest;
import com.springsecuritydemo.entity.response.JSGridReturnData;

public interface LpmApiLogService {

	public ResponseEntity<JSGridReturnData<LpmApiLog>> queryLpmApiLog(ConditionsRequest conditionsRequest) throws ParseException;

	public LpmApiLog findByLpmid(Long lpmid);
	
}

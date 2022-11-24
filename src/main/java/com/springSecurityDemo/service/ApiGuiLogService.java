package com.springSecurityDemo.service;

import java.text.ParseException;

import org.springframework.http.ResponseEntity;

import com.springSecurityDemo.entity.ApiGuiLog;
import com.springSecurityDemo.entity.request.ConditionsRequest;

public interface ApiGuiLogService {

	public ResponseEntity<?> queryApiGuiLog(ConditionsRequest conditionsRequest) throws ParseException;

	public ApiGuiLog findByApiGuiId(Long lpmid);
	
	public void save(ApiGuiLog apiGuiLog);
}

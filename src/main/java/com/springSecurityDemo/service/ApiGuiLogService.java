package com.springsecuritydemo.service;

import java.text.ParseException;

import org.springframework.http.ResponseEntity;

import com.springsecuritydemo.entity.ApiGuiLog;
import com.springsecuritydemo.entity.request.ConditionsRequest;
import com.springsecuritydemo.entity.response.JSGridReturnData;

public interface ApiGuiLogService {

	public ResponseEntity<JSGridReturnData<ApiGuiLog>> queryApiGuiLog(ConditionsRequest conditionsRequest) throws ParseException;

	public ApiGuiLog findByApiGuiId(Long lpmid);
	
	public void save(ApiGuiLog apiGuiLog);
}

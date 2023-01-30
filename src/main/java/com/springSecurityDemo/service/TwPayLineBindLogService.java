package com.springsecuritydemo.service;

import java.text.ParseException;

import org.springframework.http.ResponseEntity;

import com.springsecuritydemo.entity.TwPayLineBindLog;
import com.springsecuritydemo.entity.request.ConditionsRequest;
import com.springsecuritydemo.entity.response.JSGridReturnData;


public interface TwPayLineBindLogService {
	
	public ResponseEntity<JSGridReturnData<TwPayLineBindLog>> querytwPayLineBindLog(ConditionsRequest conditionsRequest) throws ParseException;

	public TwPayLineBindLog findBytwPayLineBindLogId(Long twPayLineBindLogid);

}

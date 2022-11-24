package com.springSecurityDemo.service;

import java.text.ParseException;

import org.springframework.http.ResponseEntity;

import com.springSecurityDemo.entity.TwPayLineBindLog;
import com.springSecurityDemo.entity.request.ConditionsRequest;


public interface TwPayLineBindLogService {
	
	public ResponseEntity<?> querytwPayLineBindLog(ConditionsRequest conditionsRequest) throws ParseException;

	public TwPayLineBindLog findBytwPayLineBindLogId(Long twPayLineBindLogid);

}

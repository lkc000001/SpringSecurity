package com.springsecuritydemo.service;

import java.text.ParseException;

import org.springframework.http.ResponseEntity;

import com.springsecuritydemo.entity.Hpg5000ApiLog;
import com.springsecuritydemo.entity.request.ConditionsRequest;
import com.springsecuritydemo.entity.response.JSGridReturnData;

public interface Hpg5000ApiLogService {
	
	public ResponseEntity<JSGridReturnData<Hpg5000ApiLog>> queryHpg5000ApiLog(ConditionsRequest conditionsRequest) throws ParseException;

	public Hpg5000ApiLog findByhpgLogId(Long hpgLogId);
}

package com.springSecurityDemo.service;

import java.text.ParseException;

import org.springframework.http.ResponseEntity;

import com.springSecurityDemo.entity.Hpg5000ApiLog;
import com.springSecurityDemo.entity.request.ConditionsRequest;

public interface Hpg5000ApiLogService {
	
	public ResponseEntity<?> queryHpg5000ApiLog(ConditionsRequest conditionsRequest) throws ParseException;

	public Hpg5000ApiLog findByhpgLogId(Long hpgLogId);
}

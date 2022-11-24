package com.springSecurityDemo.service;

import java.text.ParseException;
import java.util.List;

import org.springframework.http.ResponseEntity;

import com.springSecurityDemo.entity.LpmApiLog;
import com.springSecurityDemo.entity.request.ConditionsRequest;

public interface LpmApiLogService {

	public ResponseEntity<?> queryLpmApiLog(ConditionsRequest conditionsRequest) throws ParseException;

	public LpmApiLog findByLpmid(Long lpmid);
	
}

package com.springSecurityDemo.service;

import java.text.ParseException;

import org.springframework.http.ResponseEntity;

import com.springSecurityDemo.entity.IcpApiLog;
import com.springSecurityDemo.entity.request.ConditionsRequest;

public interface IcpApiLogService {
	
	public ResponseEntity<?> queryIcpApiLog(ConditionsRequest conditionsRequest) throws ParseException;

	public IcpApiLog findByicpId(Long icpId);
	
}

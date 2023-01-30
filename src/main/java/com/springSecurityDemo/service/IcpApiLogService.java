package com.springsecuritydemo.service;

import java.text.ParseException;

import org.springframework.http.ResponseEntity;

import com.springsecuritydemo.entity.IcpApiLog;
import com.springsecuritydemo.entity.request.ConditionsRequest;
import com.springsecuritydemo.entity.response.JSGridReturnData;

public interface IcpApiLogService {
	
	public ResponseEntity<JSGridReturnData<IcpApiLog>> queryIcpApiLog(ConditionsRequest conditionsRequest) throws ParseException;

	public IcpApiLog findByicpId(Long icpId);
	
}

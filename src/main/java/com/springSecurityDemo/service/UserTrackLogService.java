package com.springSecurityDemo.service;

import java.text.ParseException;

import org.springframework.http.ResponseEntity;

import com.springSecurityDemo.entity.UserTrackLog;
import com.springSecurityDemo.entity.request.ConditionsRequest;

public interface UserTrackLogService {
	
	public ResponseEntity<?> queryUserTrackLog(ConditionsRequest conditionsRequest) throws ParseException;

	public UserTrackLog findByUserTrackId(Long userTrackId);

	public void save(UserTrackLog userTrackLog);
}

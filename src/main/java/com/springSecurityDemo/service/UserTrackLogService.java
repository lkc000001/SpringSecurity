package com.springsecuritydemo.service;

import java.text.ParseException;

import org.springframework.http.ResponseEntity;

import com.springsecuritydemo.entity.UserTrackLog;
import com.springsecuritydemo.entity.request.ConditionsRequest;
import com.springsecuritydemo.entity.response.JSGridReturnData;

public interface UserTrackLogService {
	
	public ResponseEntity<JSGridReturnData<UserTrackLog>> queryUserTrackLog(ConditionsRequest conditionsRequest) throws ParseException;

	public UserTrackLog findByUserTrackId(Long userTrackId);

	public void save(UserTrackLog userTrackLog);
}

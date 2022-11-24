package com.springSecurityDemo.service;

import java.text.ParseException;

import com.springSecurityDemo.entity.request.ConditionsRequest;

public interface ServiceUtil {
	
	void requestDateCheck(ConditionsRequest conditionsRequest) throws ParseException;
	
}

package com.springsecuritydemo.service;

import java.text.ParseException;
import java.util.Optional;

import com.springsecuritydemo.entity.request.ConditionsRequest;

public interface ServiceUtil {
	
	void requestDateCheck(ConditionsRequest conditionsRequest) throws ParseException;
	
	<T> T checkDataIsPresent(Optional<T> data);
}

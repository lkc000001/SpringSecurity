package com.springsecuritydemo.service.impl;


import java.text.ParseException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.springsecuritydemo.entity.request.ConditionsRequest;
import com.springsecuritydemo.exception.QueryNoDataException;
import com.springsecuritydemo.exception.TimeFormatException;
import com.springsecuritydemo.service.ServiceUtil;
import com.springsecuritydemo.util.DateTimtUtil;
import com.springsecuritydemo.util.ValidateUtil;


@Service
public class ServiceUtilImpl implements ServiceUtil {
	
	@Autowired
	private DateTimtUtil dateTimtUtil;
	
	@Autowired
	private ValidateUtil validateUtil;
	
	@Override
	public void requestDateCheck(ConditionsRequest conditionsRequest) throws ParseException {
		if (validateUtil.isNotBlank(conditionsRequest.getQueryDate())) {
			
			String startTime = validateUtil.isBlank(conditionsRequest.getTimeStart()) ? "00:00:00.000" : conditionsRequest.getTimeStart() + ":00.000";
			String endTime = validateUtil.isBlank(conditionsRequest.getTimeEnd()) ? "23:59:59.997" : conditionsRequest.getTimeEnd() + ":59.997";
			//檢查起始時間是否小於結束時間
			if(Integer.parseInt(startTime.substring(0, 8).replace(":", "")) > Integer.parseInt(endTime.substring(0, 8).replace(":", ""))) {
				throw new TimeFormatException("起始時間大於結束時間!!!", 400);
			}
			conditionsRequest.setStartDate(dateTimtUtil.formatStrToDate(conditionsRequest.getQueryDate() + " " + startTime));
			conditionsRequest.setEndDate(dateTimtUtil.formatStrToDate(conditionsRequest.getQueryDate() + " " + endTime));
		}
		int pageSize = conditionsRequest.getPageSize() == null ? 5 : conditionsRequest.getPageSize();
		conditionsRequest.setPageSize(pageSize);
		int getPageIndex = conditionsRequest.getPageIndex() == null ? 1 : conditionsRequest.getPageIndex();
		conditionsRequest.setPageIndex(getPageIndex);
		String sortField = validateUtil.isBlank(conditionsRequest.getSortField()) ? "lpmid" : conditionsRequest.getSortField();
		conditionsRequest.setSortField(sortField);
		String sortOrder = validateUtil.isBlank(conditionsRequest.getSortOrder()) ? "asc" : conditionsRequest.getSortOrder();
		conditionsRequest.setSortOrder(sortOrder);
		String queryData =  validateUtil.isBlank(conditionsRequest.getQueryData()) ? "" : "%" + conditionsRequest.getQueryData() + "%";
		conditionsRequest.setQueryData(queryData);
		String queryUrl =  validateUtil.isBlank(conditionsRequest.getQueryUrl()) ? "" : "%" + conditionsRequest.getQueryUrl() + "%";
		conditionsRequest.setQueryUrl(queryUrl);
	}

	@Override
	public <T> T checkDataIsPresent(Optional<T> data) {
		if (!data.isPresent()) {
    		throw new QueryNoDataException("查無資料!!!", 404);
    	}
		return data.get();
	}
}

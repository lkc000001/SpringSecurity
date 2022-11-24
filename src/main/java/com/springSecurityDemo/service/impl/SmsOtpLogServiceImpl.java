package com.springSecurityDemo.service.impl;

import java.text.ParseException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.springSecurityDemo.entity.SmsOtpLog;
import com.springSecurityDemo.entity.request.ConditionsRequest;
import com.springSecurityDemo.entity.response.JSGridResponse;
import com.springSecurityDemo.entity.response.JSGridReturnData;
import com.springSecurityDemo.exception.QueryNoDataException;
import com.springSecurityDemo.repositories.SmsOtpLogRepository;
import com.springSecurityDemo.service.ServiceUtil;
import com.springSecurityDemo.service.SmsOtpLogService;
import com.springSecurityDemo.util.DateTimtUtil;
import com.baomidou.dynamic.datasource.annotation.DS;

@DS("DBTOAPI")
@Service
public class SmsOtpLogServiceImpl implements SmsOtpLogService {

	@Autowired
	SmsOtpLogRepository smsOtpLogRepository;
	
	@Autowired
	DateTimtUtil dateTimtUtil;
	
	@Autowired
	ServiceUtil serviceUtil;
	
	/**
	 * LpmApiLog Table依查詢條件查詢,依分頁及排序Response
	 * @param lpmApiLogRequest
	 * @return ResponseEntity
	 * @throws ParseException 
	 */
    @Override
	public ResponseEntity<?> querySmsOtpLog(ConditionsRequest conditionsRequest) throws ParseException
	{
		serviceUtil.requestDateCheck(conditionsRequest);

		List<SmsOtpLog> smsOtpLogs = smsOtpLogRepository.querySmsOtpLog(conditionsRequest.getQueryUserId(), conditionsRequest.getStartDate(), 
									 conditionsRequest.getEndDate(), conditionsRequest.getQueryData(), conditionsRequest.getQueryType(), 
									 conditionsRequest.getQueryUrl());
		//分頁,排序
		if(smsOtpLogs.size() > 0) {
			int pageSize = conditionsRequest.getPageSize();
			List<SmsOtpLog> result = smsOtpLogs.stream()
				.peek(lpm -> {
						lpm.setShowDate(dateTimtUtil.formatDateToStr(lpm.getCreatetime(), "yyyy-MM-dd"));
						lpm.setShowTime(dateTimtUtil.formatDateToStr(lpm.getCreatetime(), "HH:mm:ss"));
					})
				.sorted(smsOtpLogSort(conditionsRequest.getSortField(), conditionsRequest.getSortOrder()))
				.skip(pageSize * (conditionsRequest.getPageIndex() - 1))
				.limit(pageSize)
				.collect(Collectors.toList());
			
			return new ResponseEntity<JSGridReturnData<SmsOtpLog>>(JSGridResponse.getResponseData(result, smsOtpLogs.size()), HttpStatus.OK);
		} else {
			throw new QueryNoDataException("查無資料!!!", 404);
		} 
	}

	/**
	 * 依solId查詢
	 * @param solId
	 * @return SmsOtpLog
	 */
    @Override
	public SmsOtpLog findBySolId(Long solId) {
		return smsOtpLogRepository.findById(solId).get();
	}
	
	/**
	 * 排序欄位設置
	 * @param sortField
	 * @param sortOrder
	 * @return Comparator<SmsOtpLog>
	 */
	private Comparator<SmsOtpLog> smsOtpLogSort(String sortField, String sortOrder) {
		switch(sortField) {
		case "USERID":
			return sortOrder.equals("asc") ? 
					Comparator.comparing(SmsOtpLog::getUserId) : 
						Comparator.comparing(SmsOtpLog::getUserId).reversed();
		case "PHONE":
			return sortOrder.equals("asc") ? 
					Comparator.comparing(SmsOtpLog::getPhone) : 
						Comparator.comparing(SmsOtpLog::getPhone).reversed();
		case "SYSFLAG":
			return sortOrder.equals("asc") ? 
					Comparator.comparing(SmsOtpLog::getSysFlag) : 
						Comparator.comparing(SmsOtpLog::getSysFlag).reversed();	
		}
		
		return sortOrder.equals("asc") ? 
				Comparator.comparing(SmsOtpLog::getSolId) : 
					Comparator.comparing(SmsOtpLog::getSolId).reversed();
	}
}

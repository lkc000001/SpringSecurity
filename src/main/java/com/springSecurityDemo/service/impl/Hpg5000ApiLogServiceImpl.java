package com.springSecurityDemo.service.impl;

import java.text.ParseException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.springSecurityDemo.entity.Hpg5000ApiLog;
import com.springSecurityDemo.entity.request.ConditionsRequest;
import com.springSecurityDemo.entity.response.JSGridResponse;
import com.springSecurityDemo.entity.response.JSGridReturnData;
import com.springSecurityDemo.exception.QueryNoDataException;
import com.springSecurityDemo.repositories.Hpg5000ApiLogRepository;
import com.springSecurityDemo.service.Hpg5000ApiLogService;
import com.springSecurityDemo.service.ServiceUtil;
import com.springSecurityDemo.util.DateTimtUtil;
import com.baomidou.dynamic.datasource.annotation.DS;

@DS("DBTOAPI")
@Service
public class Hpg5000ApiLogServiceImpl implements Hpg5000ApiLogService {

	@Autowired
	Hpg5000ApiLogRepository hpg5000ApiLogRepository;
	
	@Autowired
	DateTimtUtil dateTimtUtil;
	
	@Autowired
	ServiceUtil serviceUtil;
	
	/**
	 * Hpg5000ApiLog Table依查詢條件查詢,依分頁及排序Response
	 * @param ConditionsRequest
	 * @return ResponseEntity
	 * @throws ParseException 
	 */
    @Override
	public ResponseEntity<?> queryHpg5000ApiLog(ConditionsRequest conditionsRequest) throws ParseException
	{
		serviceUtil.requestDateCheck(conditionsRequest);
		
		List<Hpg5000ApiLog> hpg5000ApiLogs = hpg5000ApiLogRepository.queryHpg5000ApiLog(conditionsRequest.getQueryUserId(), conditionsRequest.getStartDate(), 
										 conditionsRequest.getEndDate(), conditionsRequest.getQueryData(), conditionsRequest.getQueryType(), 
										 conditionsRequest.getQueryUrl());
		if(hpg5000ApiLogs.size() > 0 ) {
		//分頁,排序
		int pageSize = conditionsRequest.getPageSize();
		List<Hpg5000ApiLog> result = hpg5000ApiLogs.stream()
			.peek(Hpg5000 -> {
					Hpg5000.setShowDate(dateTimtUtil.formatDateToStr(Hpg5000.getCreatetime(), "yyyy-MM-dd"));
					Hpg5000.setShowTime(dateTimtUtil.formatDateToStr(Hpg5000.getCreatetime(), "HH:mm:ss"));
				})
			.sorted(hpg5000ApiLogSort(conditionsRequest.getSortField(), conditionsRequest.getSortOrder()))
			.skip(pageSize * (conditionsRequest.getPageIndex() - 1))
			.limit(pageSize)
			.collect(Collectors.toList());
		
			return new ResponseEntity<JSGridReturnData<Hpg5000ApiLog>>(JSGridResponse.getResponseData(result, hpg5000ApiLogs.size()), HttpStatus.OK);
		} else {
			throw new QueryNoDataException("查無資料!!!", 404);
		}
	}

	/**
	 * 依hpgLogId查詢
	 * @param hpgLogId
	 * @return Hpg5000ApiLog
	 */
    @Override
	public Hpg5000ApiLog findByhpgLogId(Long hpgLogId) {
		return hpg5000ApiLogRepository.findById(hpgLogId).get();
	}
	
	/**
	 * 排序欄位設置
	 * @param sortField
	 * @param sortOrder
	 * @return Comparator<Hpg5000ApiLog>
	 */
	private Comparator<Hpg5000ApiLog> hpg5000ApiLogSort(String sortField, String sortOrder) {
		switch(sortField) {
		case "HPGLogIdnTxn":
			return sortOrder.equals("asc") ? 
					Comparator.comparing(Hpg5000ApiLog::getHpgLogIdnTxn) : 
						Comparator.comparing(Hpg5000ApiLog::getHpgLogIdnTxn).reversed();
		case "HPGLogSeqNo":
			return sortOrder.equals("asc") ? 
					Comparator.comparing(Hpg5000ApiLog::getHpgLogSeqNo) : 
						Comparator.comparing(Hpg5000ApiLog::getHpgLogSeqNo).reversed();
		case "HPGLogTxnTime":
			return sortOrder.equals("asc") ? 
					Comparator.comparing(Hpg5000ApiLog::getHpgLogTxnTime) : 
						Comparator.comparing(Hpg5000ApiLog::getHpgLogTxnTime).reversed();	
		}
		
		return sortOrder.equals("asc") ? 
				Comparator.comparing(Hpg5000ApiLog::getHpgLogId) : 
					Comparator.comparing(Hpg5000ApiLog::getHpgLogId).reversed();
	}
}

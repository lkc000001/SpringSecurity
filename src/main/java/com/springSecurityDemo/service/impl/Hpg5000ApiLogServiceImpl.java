package com.springsecuritydemo.service.impl;

import java.text.ParseException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.springsecuritydemo.entity.Hpg5000ApiLog;
import com.springsecuritydemo.entity.request.ConditionsRequest;
import com.springsecuritydemo.entity.response.JSGridResponse;
import com.springsecuritydemo.entity.response.JSGridReturnData;
import com.springsecuritydemo.exception.QueryNoDataException;
import com.springsecuritydemo.repositories.Hpg5000ApiLogRepository;
import com.springsecuritydemo.service.Hpg5000ApiLogService;
import com.springsecuritydemo.service.ServiceUtil;

@DS("DBTOAPI")
@Service
public class Hpg5000ApiLogServiceImpl implements Hpg5000ApiLogService {

	@Autowired
	private Hpg5000ApiLogRepository hpg5000ApiLogRepository;
	
	@Autowired
	private ServiceUtil serviceUtil;
	
	/**
	 * Hpg5000ApiLog Table依查詢條件查詢,依分頁及排序Response
	 * @param ConditionsRequest
	 * @return ResponseEntity
	 * @throws ParseException 
	 */
    @Override
	public ResponseEntity<JSGridReturnData<Hpg5000ApiLog>> queryHpg5000ApiLog(ConditionsRequest conditionsRequest) throws ParseException
	{
serviceUtil.requestDateCheck(conditionsRequest);
		
		List<Hpg5000ApiLog> hpg5000ApiLogs = hpg5000ApiLogRepository.queryHpg5000ApiLog(conditionsRequest.getQueryUserId(), conditionsRequest.getStartDate(), 
										 conditionsRequest.getEndDate(), conditionsRequest.getQueryData(), conditionsRequest.getQueryType(), 
										 conditionsRequest.getQueryUrl(), conditionsRequest.getPageIndex(), conditionsRequest.getPageSize());
		if(hpg5000ApiLogs.size() > 0 ) {
		//排序
		List<Hpg5000ApiLog> result = hpg5000ApiLogs.stream()
			.sorted(hpg5000ApiLogSort(conditionsRequest.getSortField(), conditionsRequest.getSortOrder()))
			.collect(Collectors.toList());
		
			return new ResponseEntity<>(JSGridResponse.getResponseData(result, hpg5000ApiLogRepository.count()), HttpStatus.OK);
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
		return serviceUtil.checkDataIsPresent(hpg5000ApiLogRepository.findById(hpgLogId));
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
		default:
			return sortOrder.equals("asc") ? 
				Comparator.comparing(Hpg5000ApiLog::getHpgLogId) : 
					Comparator.comparing(Hpg5000ApiLog::getHpgLogId).reversed();
		}
	}
}

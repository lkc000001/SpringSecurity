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
import com.springsecuritydemo.entity.LpmApiLog;
import com.springsecuritydemo.entity.request.ConditionsRequest;
import com.springsecuritydemo.entity.response.JSGridResponse;
import com.springsecuritydemo.entity.response.JSGridReturnData;
import com.springsecuritydemo.exception.QueryNoDataException;
import com.springsecuritydemo.repositories.LpmApiLogRepository;
import com.springsecuritydemo.service.LpmApiLogService;
import com.springsecuritydemo.service.ServiceUtil;

@DS("DBTLPM")
@Service
public class LpmApiLogServiceImpl implements LpmApiLogService {

	@Autowired
	private LpmApiLogRepository lpmApiLogRepository;
	
	@Autowired
	private ServiceUtil serviceUtil;
	
	/**
	 * LpmApiLog Table依查詢條件查詢,依分頁及排序Response
	 * @param ConditionsRequest
	 * @return ResponseEntity
	 * @throws ParseException 
	 */
    @Override
	public ResponseEntity<JSGridReturnData<LpmApiLog>> queryLpmApiLog(ConditionsRequest conditionsRequest) throws ParseException
	{
		serviceUtil.requestDateCheck(conditionsRequest);
		
		List<LpmApiLog> lpmApiLogs = lpmApiLogRepository.getLpmApiLog(conditionsRequest.getQueryUserId(), conditionsRequest.getStartDate(), 
				 conditionsRequest.getEndDate(), conditionsRequest.getQueryData(), conditionsRequest.getQueryType(), 
				 conditionsRequest.getQueryUrl(), conditionsRequest.getPageIndex(), conditionsRequest.getPageSize());

		//排序
		if(lpmApiLogs.size() > 0 ) {
			List<LpmApiLog> result = lpmApiLogs.stream()
				.sorted(lpmApiLogSort(conditionsRequest.getSortField(), conditionsRequest.getSortOrder()))
				.collect(Collectors.toList());
		
			return new ResponseEntity<>(JSGridResponse.getResponseData(result, lpmApiLogRepository.count()), HttpStatus.OK);
		} else {
			throw new QueryNoDataException("查無資料!!!", 404);
		}
	}

	/**
	 * 依Lpmid查詢
	 * @param lpmid
	 * @return LpmApiLog
	 */
    @Override
	public LpmApiLog findByLpmid(Long lpmId) {
    	return serviceUtil.checkDataIsPresent(lpmApiLogRepository.findById(lpmId));
	}
	
	/**
	 * 排序欄位設置
	 * @param sortField
	 * @param sortOrder
	 * @return Comparator<LpmApiLog>
	 */
	private Comparator<LpmApiLog> lpmApiLogSort(String sortField, String sortOrder) {
		switch(sortField) {
		case "lpmtxseq":
			return sortOrder.equals("asc") ? 
					Comparator.comparing(LpmApiLog::getLpmTxSeq) : 
						Comparator.comparing(LpmApiLog::getLpmTxSeq).reversed();
		case "lpmuserid":
			return sortOrder.equals("asc") ? 
					Comparator.comparing(LpmApiLog::getLpmUserId) : 
						Comparator.comparing(LpmApiLog::getLpmUserId).reversed();
		case "lpmusercode":
			return sortOrder.equals("asc") ? 
					Comparator.comparing(LpmApiLog::getLpmUserCode) : 
						Comparator.comparing(LpmApiLog::getLpmUserCode).reversed();
		default:
			return sortOrder.equals("asc") ? 
				Comparator.comparing(LpmApiLog::getLpmId) : 
					Comparator.comparing(LpmApiLog::getLpmId).reversed();
		}
	}
}

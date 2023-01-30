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
import com.springsecuritydemo.entity.TwPayLineBindLog;
import com.springsecuritydemo.entity.request.ConditionsRequest;
import com.springsecuritydemo.entity.response.JSGridResponse;
import com.springsecuritydemo.entity.response.JSGridReturnData;
import com.springsecuritydemo.exception.QueryNoDataException;
import com.springsecuritydemo.repositories.TwPayLineBindLogRepository;
import com.springsecuritydemo.service.ServiceUtil;
import com.springsecuritydemo.service.TwPayLineBindLogService;

@DS("DBTOAPI")
@Service
public class TwPayLineBindLogServiceImpl implements TwPayLineBindLogService {

	@Autowired
	private TwPayLineBindLogRepository twPayLineBindLogRepository;
	
	@Autowired
	private ServiceUtil serviceUtil;
	
	/**
	 * LpmApiLog Table依查詢條件查詢,依分頁及排序Response
	 * @param conditionsRequest
	 * @return ResponseEntity
	 * @throws ParseException 
	 */
    @Override
	public ResponseEntity<JSGridReturnData<TwPayLineBindLog>> querytwPayLineBindLog(ConditionsRequest conditionsRequest) throws ParseException
	{
		serviceUtil.requestDateCheck(conditionsRequest);
		
		List<TwPayLineBindLog> twPayLineBindLogs = twPayLineBindLogRepository.querytwPayLineBindLog(conditionsRequest.getQueryId(), conditionsRequest.getStartDate(), 
					conditionsRequest.getEndDate(), conditionsRequest.getQueryData(), conditionsRequest.getQueryType(), 
					conditionsRequest.getQueryUrl(), conditionsRequest.getPageIndex(), conditionsRequest.getPageSize());
		//排序
		if(twPayLineBindLogs.size() > 0 ) {
			List<TwPayLineBindLog> result = twPayLineBindLogs.stream()
			.sorted(twPayLineBindLogSort(conditionsRequest.getSortField(), conditionsRequest.getSortOrder()))
			.collect(Collectors.toList());
			
			return new ResponseEntity<>(JSGridResponse.getResponseData(result, twPayLineBindLogRepository.count()), HttpStatus.OK);
		} else {
			throw new QueryNoDataException("查無資料!!!", 404);
		}
	}

	/**
	 * 依twPayLineBindLogid查詢
	 * @param twPayLineBindLogid
	 * @return TwPayLineBindLog
	 */
    @Override
	public TwPayLineBindLog findBytwPayLineBindLogId(Long twPayLineBindLogid) {
		return serviceUtil.checkDataIsPresent(twPayLineBindLogRepository.findById(twPayLineBindLogid));
	}
	
	/**
	 * 排序欄位設置
	 * @param sortField
	 * @param sortOrder
	 * @return Comparator<TwPayLineBindLog>
	 */
	private Comparator<TwPayLineBindLog> twPayLineBindLogSort(String sortField, String sortOrder) {
		switch(sortField) {
		case "TWPayLineBindLogLineUid":
			return sortOrder.equals("asc") ? 
					Comparator.comparing(TwPayLineBindLog::getTwPayLineBindLogLineUid) : 
						Comparator.comparing(TwPayLineBindLog::getTwPayLineBindLogLineUid).reversed();
		case "TWPayLineBindLogTxnNo":
			return sortOrder.equals("asc") ? 
					Comparator.comparing(TwPayLineBindLog::getTwPayLineBindLogTxnNo) : 
						Comparator.comparing(TwPayLineBindLog::getTwPayLineBindLogTxnNo).reversed();
		case "TWPayLineBindLogTxnDateTime":
			return sortOrder.equals("asc") ? 
					Comparator.comparing(TwPayLineBindLog::getTwPayLineBindLogTxnDateTime) : 
						Comparator.comparing(TwPayLineBindLog::getTwPayLineBindLogTxnDateTime).reversed();
		default:
			return sortOrder.equals("asc") ? 
				Comparator.comparing(TwPayLineBindLog::getTwPayLineBindLogId) : 
					Comparator.comparing(TwPayLineBindLog::getTwPayLineBindLogId).reversed();
		}
	}
}

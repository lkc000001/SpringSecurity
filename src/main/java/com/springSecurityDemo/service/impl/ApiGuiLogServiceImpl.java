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
import com.springsecuritydemo.entity.ApiGuiLog;
import com.springsecuritydemo.entity.request.ConditionsRequest;
import com.springsecuritydemo.entity.response.JSGridResponse;
import com.springsecuritydemo.entity.response.JSGridReturnData;
import com.springsecuritydemo.exception.QueryNoDataException;
import com.springsecuritydemo.repositories.ApiGuiLogRepository;
import com.springsecuritydemo.service.ApiGuiLogService;
import com.springsecuritydemo.service.ServiceUtil;

@DS("DBAPIGL")
@Service
public class ApiGuiLogServiceImpl implements ApiGuiLogService {

	@Autowired
	private ApiGuiLogRepository apiGuiLogRepository;
	
	@Autowired
	private ServiceUtil serviceUtil;
	
	/**
	 * ApiGuiLog Table依查詢條件查詢,依分頁及排序Response
	 * @param ConditionsRequest
	 * @return ResponseEntity
	 * @throws ParseException 
	 */
    @Override
	public ResponseEntity<JSGridReturnData<ApiGuiLog>> queryApiGuiLog(ConditionsRequest conditionsRequest) throws ParseException
	{
serviceUtil.requestDateCheck(conditionsRequest);
		
		List<ApiGuiLog> apiGuiLogs = apiGuiLogRepository.queryApiGuiLog(conditionsRequest.getQueryUserId(), conditionsRequest.getStartDate(), 
									 conditionsRequest.getEndDate(), conditionsRequest.getQueryData(), conditionsRequest.getQueryType(), 
									 conditionsRequest.getQueryUrl(), conditionsRequest.getPageIndex(), conditionsRequest.getPageSize());
		
		//排序
		int count = apiGuiLogs.size();
		if(count > 0 ) {
			List<ApiGuiLog> result = apiGuiLogs.stream()
				.sorted(apiGuiLogSort(conditionsRequest.getSortField(), conditionsRequest.getSortOrder()))
				.collect(Collectors.toList());
			
			return new ResponseEntity<>(JSGridResponse.getResponseData(result, count), HttpStatus.OK);
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
	public ApiGuiLog findByApiGuiId(Long apiGuiId) {
    	return serviceUtil.checkDataIsPresent(apiGuiLogRepository.findById(apiGuiId));
	}
	
    /**
     * 儲存LOG
     * @param ApiGuiLog
     */
	@Override
	public void save(ApiGuiLog apiGuiLog) {
		apiGuiLogRepository.save(apiGuiLog);
	}
	
	/**
	 * 排序欄位設置
	 * @param sortField
	 * @param sortOrder
	 * @return Comparator<ApiGuiLog>
	 */
	private Comparator<ApiGuiLog> apiGuiLogSort(String sortField, String sortOrder) {
		switch(sortField) {
		case "ApiGuiUserId":
			return sortOrder.equals("asc") ? 
					Comparator.comparing(ApiGuiLog::getApiGuiUserId) : 
						Comparator.comparing(ApiGuiLog::getApiGuiUserId).reversed();
		case "ApiGuiUrl":
			return sortOrder.equals("asc") ? 
					Comparator.comparing(ApiGuiLog::getApiGuiUrl) : 
						Comparator.comparing(ApiGuiLog::getApiGuiUrl).reversed();
		case "ApiGuiSendStatus":
			return sortOrder.equals("asc") ? 
					Comparator.comparing(ApiGuiLog::getApiGuiSendStatus) : 
						Comparator.comparing(ApiGuiLog::getApiGuiSendStatus).reversed();
		default:
			return sortOrder.equals("asc") ? 
				Comparator.comparing(ApiGuiLog::getApiGuiId) : 
					Comparator.comparing(ApiGuiLog::getApiGuiId).reversed();
		}
	}
	
}

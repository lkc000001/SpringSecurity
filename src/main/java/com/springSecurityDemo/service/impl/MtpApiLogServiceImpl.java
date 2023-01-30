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
import com.springsecuritydemo.entity.MtpApiLog;
import com.springsecuritydemo.entity.request.ConditionsRequest;
import com.springsecuritydemo.entity.response.JSGridResponse;
import com.springsecuritydemo.entity.response.JSGridReturnData;
import com.springsecuritydemo.exception.QueryNoDataException;
import com.springsecuritydemo.repositories.MtpApiLogRepository;
import com.springsecuritydemo.service.MtpApiLogService;
import com.springsecuritydemo.service.ServiceUtil;

@DS("DBTMTP")
@Service
public class MtpApiLogServiceImpl implements MtpApiLogService {

	@Autowired
	private MtpApiLogRepository mtpAPILogRepository;
	
	@Autowired
	private ServiceUtil serviceUtil;
	
	/**
	 * MtpApiLog Table依查詢條件查詢,依分頁及排序Response
	 * @param ConditionsRequest
	 * @return ResponseEntity
	 * @throws ParseException 
	 */
    @Override
	public ResponseEntity<JSGridReturnData<MtpApiLog>> queryMtpApiLog(ConditionsRequest conditionsRequest) throws ParseException
	{
		serviceUtil.requestDateCheck(conditionsRequest);
		
		List<MtpApiLog> mtpApiLogs = mtpAPILogRepository.queryMtpApiLog(conditionsRequest.getQueryUserId(), conditionsRequest.getStartDate(), 
				 conditionsRequest.getEndDate(), conditionsRequest.getQueryData(), conditionsRequest.getQueryType(), 
				 conditionsRequest.getQueryUrl(), conditionsRequest.getPageIndex(), conditionsRequest.getPageSize());
		if(mtpApiLogs.size() > 0 ) {
		//排序
			List<MtpApiLog> result = mtpApiLogs.stream()
					.sorted(mtpApiLogSort(conditionsRequest.getSortField(), conditionsRequest.getSortOrder()))
					.collect(Collectors.toList());
		
			return new ResponseEntity<>(JSGridResponse.getResponseData(result, mtpAPILogRepository.count()), HttpStatus.OK);
		} else {
			throw new QueryNoDataException("查無資料!!!", 404);
		}
	}

	/**
	 * 依mtpLId查詢
	 * @param mtpLId
	 * @return MtpApiLog
	 */
    @Override
	public MtpApiLog findBymtpLId(Long mtpLId) {
		return serviceUtil.checkDataIsPresent(mtpAPILogRepository.findById(mtpLId));
	}
    
	/**
	 * 排序欄位設置
	 * @param sortField
	 * @param sortOrder
	 * @return Comparator<MtpApiLog>
	 */
	private Comparator<MtpApiLog> mtpApiLogSort(String sortField, String sortOrder) {
		switch(sortField) {
		case "MTPMobilePhone":
			return sortOrder.equals("asc") ? 
					Comparator.comparing(MtpApiLog::getMtpMobilePhone) : 
						Comparator.comparing(MtpApiLog::getMtpMobilePhone).reversed();
		case "MTPBankCode":
			return sortOrder.equals("asc") ? 
					Comparator.comparing(MtpApiLog::getMtpBankCode) : 
						Comparator.comparing(MtpApiLog::getMtpBankCode).reversed();
		default:
			return sortOrder.equals("asc") ? 
				Comparator.comparing(MtpApiLog::getMtpLId) : 
					Comparator.comparing(MtpApiLog::getMtpLId).reversed();
		}
	}
}

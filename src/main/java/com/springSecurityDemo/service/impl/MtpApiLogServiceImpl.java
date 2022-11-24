package com.springSecurityDemo.service.impl;

import java.text.ParseException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.springSecurityDemo.entity.MtpApiLog;
import com.springSecurityDemo.entity.request.ConditionsRequest;
import com.springSecurityDemo.entity.response.JSGridResponse;
import com.springSecurityDemo.entity.response.JSGridReturnData;
import com.springSecurityDemo.exception.QueryNoDataException;
import com.springSecurityDemo.repositories.MtpApiLogRepository;
import com.springSecurityDemo.service.MtpApiLogService;
import com.springSecurityDemo.service.ServiceUtil;
import com.springSecurityDemo.util.DateTimtUtil;

@DS("DBTMTP")
@Service
public class MtpApiLogServiceImpl implements MtpApiLogService {

	@Autowired
	MtpApiLogRepository mtpAPILogRepository;
	
	@Autowired
	DateTimtUtil dateTimtUtil;
	
	@Autowired
	ServiceUtil serviceUtil;
	
	/**
	 * MtpApiLog Table依查詢條件查詢,依分頁及排序Response
	 * @param ConditionsRequest
	 * @return ResponseEntity
	 * @throws ParseException 
	 */
    @Override
	public ResponseEntity<?> queryMtpApiLog(ConditionsRequest conditionsRequest) throws ParseException
	{
		serviceUtil.requestDateCheck(conditionsRequest);

		List<MtpApiLog> mtpApiLogs = mtpAPILogRepository.queryMtpApiLog(conditionsRequest.getQueryUserId(), conditionsRequest.getStartDate(), 
									 conditionsRequest.getEndDate(), conditionsRequest.getQueryData(), conditionsRequest.getQueryType(), 
									 conditionsRequest.getQueryUrl());
		if(mtpApiLogs.size() > 0 ) {
		//分頁,排序
		int pageSize = conditionsRequest.getPageSize();
		List<MtpApiLog> result = mtpApiLogs.stream()
			.peek(lpm -> {
					lpm.setShowDate(dateTimtUtil.formatDateToStr(lpm.getCreatetime(), "yyyy-MM-dd"));
					lpm.setShowTime(dateTimtUtil.formatDateToStr(lpm.getCreatetime(), "HH:mm:ss"));
				})
			.sorted(mtpApiLogSort(conditionsRequest.getSortField(), conditionsRequest.getSortOrder()))
			.skip(pageSize * (conditionsRequest.getPageIndex() - 1))
			.limit(pageSize)
			.collect(Collectors.toList());
		
			return new ResponseEntity<JSGridReturnData<MtpApiLog>>(JSGridResponse.getResponseData(result, mtpApiLogs.size()), HttpStatus.OK);
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
		return mtpAPILogRepository.findById(mtpLId).get();
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
		}
		
		return sortOrder.equals("asc") ? 
				Comparator.comparing(MtpApiLog::getMtpLId) : 
					Comparator.comparing(MtpApiLog::getMtpLId).reversed();
	}
}

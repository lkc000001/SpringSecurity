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
import com.springSecurityDemo.entity.IcpApiLog;
import com.springSecurityDemo.entity.request.ConditionsRequest;
import com.springSecurityDemo.entity.response.JSGridResponse;
import com.springSecurityDemo.entity.response.JSGridReturnData;
import com.springSecurityDemo.exception.QueryNoDataException;
import com.springSecurityDemo.repositories.IcpApiLogRepository;
import com.springSecurityDemo.service.IcpApiLogService;
import com.springSecurityDemo.service.ServiceUtil;
import com.springSecurityDemo.util.DateTimtUtil;

@DS("DBTICP")
@Service
public class IcpApiLogServiceImpl implements IcpApiLogService {

	@Autowired
	IcpApiLogRepository icpAPILogRepository;
	
	@Autowired
	DateTimtUtil dateTimtUtil;
	
	@Autowired
	ServiceUtil serviceUtil;
	
	/**
	 * IcpApiLog Table依查詢條件查詢,依分頁及排序Response
	 * @param ConditionsRequest
	 * @return ResponseEntity
	 * @throws ParseException 
	 */
    @Override
	public ResponseEntity<?> queryIcpApiLog(ConditionsRequest conditionsRequest) throws ParseException
	{
		serviceUtil.requestDateCheck(conditionsRequest);
		
		List<IcpApiLog> icpApiLogs = icpAPILogRepository.queryIcpApiLog(conditionsRequest.getQueryUserId(), conditionsRequest.getStartDate(), 
									 conditionsRequest.getEndDate(), conditionsRequest.getQueryData(), conditionsRequest.getQueryType(), 
									 conditionsRequest.getQueryUrl());
		if(icpApiLogs.size() > 0 ) {
			//分頁,排序
			int pageSize = conditionsRequest.getPageSize();
			List<IcpApiLog> result = icpApiLogs.stream()
				.peek(lpm -> {
						lpm.setShowDate(dateTimtUtil.formatDateToStr(lpm.getCreatetime(), "yyyy-MM-dd"));
						lpm.setShowTime(dateTimtUtil.formatDateToStr(lpm.getCreatetime(), "HH:mm:ss"));
					})
				.sorted(icpApiLogSort(conditionsRequest.getSortField(), conditionsRequest.getSortOrder()))
				.skip(pageSize * (conditionsRequest.getPageIndex() - 1))
				.limit(pageSize)
				.collect(Collectors.toList());
			
			return new ResponseEntity<JSGridReturnData<IcpApiLog>>(JSGridResponse.getResponseData(result, icpApiLogs.size()), HttpStatus.OK);
		} else {
			throw new QueryNoDataException("查無資料!!!", 404);
		}
	}

	/**
	 * 依icpId查詢
	 * @param icpId
	 * @return IcpApiLog
	 */
    @Override
	public IcpApiLog findByicpId(Long icpId) {
		return icpAPILogRepository.findById(icpId).get();
	}
	
	/**
	 * 排序欄位設置
	 * @param sortField
	 * @param sortOrder
	 * @return Comparator<IcpApiLog>
	 */
	private Comparator<IcpApiLog> icpApiLogSort(String sortField, String sortOrder) {
		switch(sortField) {
		case "ICPCustId":
			return sortOrder.equals("asc") ? 
					Comparator.comparing(IcpApiLog::getIcpCustId) : 
						Comparator.comparing(IcpApiLog::getIcpCustId).reversed();
		case "ICPType":
			return sortOrder.equals("asc") ? 
					Comparator.comparing(IcpApiLog::getIcpType) : 
						Comparator.comparing(IcpApiLog::getIcpType).reversed();
		}
		
		return sortOrder.equals("asc") ? 
				Comparator.comparing(IcpApiLog::getIcpId) : 
					Comparator.comparing(IcpApiLog::getIcpId).reversed();
	}
}

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
import com.springsecuritydemo.entity.IcpApiLog;
import com.springsecuritydemo.entity.request.ConditionsRequest;
import com.springsecuritydemo.entity.response.JSGridResponse;
import com.springsecuritydemo.entity.response.JSGridReturnData;
import com.springsecuritydemo.exception.QueryNoDataException;
import com.springsecuritydemo.repositories.IcpApiLogRepository;
import com.springsecuritydemo.service.IcpApiLogService;
import com.springsecuritydemo.service.ServiceUtil;

@DS("DBTICP")
@Service
public class IcpApiLogServiceImpl implements IcpApiLogService {

	@Autowired
	private IcpApiLogRepository icpAPILogRepository;
	
	@Autowired
	private ServiceUtil serviceUtil;
	
	/**
	 * IcpApiLog Table依查詢條件查詢,依分頁及排序Response
	 * @param ConditionsRequest
	 * @return ResponseEntity
	 * @throws ParseException 
	 */
    @Override
	public ResponseEntity<JSGridReturnData<IcpApiLog>> queryIcpApiLog(ConditionsRequest conditionsRequest) throws ParseException
	{
serviceUtil.requestDateCheck(conditionsRequest);
		
		List<IcpApiLog> icpApiLogs = icpAPILogRepository.queryIcpApiLog(conditionsRequest.getQueryUserId(), conditionsRequest.getStartDate(), 
									 conditionsRequest.getEndDate(), conditionsRequest.getQueryData(), conditionsRequest.getQueryType(), 
									 conditionsRequest.getQueryUrl(), conditionsRequest.getPageIndex(), conditionsRequest.getPageSize());
		int count = icpApiLogs.size();
		if(count > 0 ) {
			//排序
			List<IcpApiLog> result = icpApiLogs.stream()
				.sorted(icpApiLogSort(conditionsRequest.getSortField(), conditionsRequest.getSortOrder()))
				.collect(Collectors.toList());
			
			return new ResponseEntity<>(JSGridResponse.getResponseData(result, count), HttpStatus.OK);
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
		return serviceUtil.checkDataIsPresent(icpAPILogRepository.findById(icpId));
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
		default:
			return sortOrder.equals("asc") ? 
				Comparator.comparing(IcpApiLog::getIcpId) : 
					Comparator.comparing(IcpApiLog::getIcpId).reversed();
		}
		
		
	}
}

package com.springSecurityDemo.service.impl;

import java.text.ParseException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.transform.Source;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.springSecurityDemo.entity.LpmApiLog;
import com.springSecurityDemo.entity.TwPayLineBindLog;
import com.springSecurityDemo.entity.request.ConditionsRequest;
import com.springSecurityDemo.entity.response.JSGridResponse;
import com.springSecurityDemo.entity.response.JSGridReturnData;
import com.springSecurityDemo.exception.QueryNoDataException;
import com.springSecurityDemo.repositories.LpmApiLogRepository;
import com.springSecurityDemo.service.LpmApiLogService;
import com.springSecurityDemo.service.ServiceUtil;
import com.springSecurityDemo.util.DateTimtUtil;

@DS("DBTLPM")
@Service
public class LpmApiLogServiceImpl implements LpmApiLogService {

	@Autowired
	LpmApiLogRepository lpmApiLogRepository;
	
	@Autowired
	DateTimtUtil dateTimtUtil;
	
	@Autowired
	ServiceUtil serviceUtil;
	
	/**
	 * LpmApiLog Table依查詢條件查詢,依分頁及排序Response
	 * @param ConditionsRequest
	 * @return ResponseEntity
	 * @throws ParseException 
	 */
    @Override
	public ResponseEntity<?> queryLpmApiLog(ConditionsRequest conditionsRequest) throws ParseException
	{
		serviceUtil.requestDateCheck(conditionsRequest);
		
		List<LpmApiLog> lpmApiLogs = lpmApiLogRepository.getLpmApiLog(conditionsRequest.getQueryUserId(), conditionsRequest.getStartDate(), 
									 conditionsRequest.getEndDate(), conditionsRequest.getQueryData(), conditionsRequest.getQueryType(), 
									 conditionsRequest.getQueryUrl());
		//分頁,排序
		if(lpmApiLogs.size() > 0 ) {
			int pageSize = conditionsRequest.getPageSize();
			List<LpmApiLog> result = lpmApiLogs.stream()
				.peek(lpm -> {
						lpm.setShowDate(dateTimtUtil.formatDateToStr(lpm.getCreatetime(), "yyyy-MM-dd"));
						lpm.setShowTime(dateTimtUtil.formatDateToStr(lpm.getCreatetime(), "HH:mm:ss"));
					})
				.sorted(lpmApiLogSort(conditionsRequest.getSortField(), conditionsRequest.getSortOrder()))
				.skip(pageSize * (conditionsRequest.getPageIndex() - 1))
				.limit(pageSize)
				.collect(Collectors.toList());
			return new ResponseEntity<JSGridReturnData<LpmApiLog>>(JSGridResponse.getResponseData(result, lpmApiLogs.size()), HttpStatus.OK);
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
    	return lpmApiLogRepository.findById(lpmId).get();
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
		}
		
		return sortOrder.equals("asc") ? 
				Comparator.comparing(LpmApiLog::getLpmId) : 
					Comparator.comparing(LpmApiLog::getLpmId).reversed();
	}
}

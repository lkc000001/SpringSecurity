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
import com.springSecurityDemo.entity.RewardAPILog;
import com.springSecurityDemo.entity.request.ConditionsRequest;
import com.springSecurityDemo.entity.response.JSGridResponse;
import com.springSecurityDemo.entity.response.JSGridReturnData;
import com.springSecurityDemo.exception.QueryNoDataException;
import com.springSecurityDemo.repositories.RewardApiLogRepository;
import com.springSecurityDemo.service.RewardApiLogService;
import com.springSecurityDemo.service.ServiceUtil;
import com.springSecurityDemo.util.DateTimtUtil;

@DS("DBTREWARD")
@Service
public class RewardApiLogServiceImpl implements RewardApiLogService {

	@Autowired
	RewardApiLogRepository rewardAPILogRepository;
	
	@Autowired
	DateTimtUtil dateTimtUtil;
	
	@Autowired
	ServiceUtil serviceUtil;
	
	/**
	 * RewardAPILog Table依查詢條件查詢,依分頁及排序Response
	 * @param ConditionsRequest
	 * @return ResponseEntity
	 * @throws ParseException 
	 */
    @Override
	public ResponseEntity<?> queryRewardAPILog(ConditionsRequest conditionsRequest) throws ParseException
	{
		serviceUtil.requestDateCheck(conditionsRequest);
		
		List<RewardAPILog> rewardAPILogs = rewardAPILogRepository.queryRewardAPILog(conditionsRequest.getQueryUserId(), conditionsRequest.getStartDate(), 
										conditionsRequest.getEndDate(), conditionsRequest.getQueryData(), conditionsRequest.getQueryType(), 
										conditionsRequest.getQueryUrl());
		if(rewardAPILogs.size() > 0 ) {
			//分頁,排序
			int pageSize = conditionsRequest.getPageSize();
			List<RewardAPILog> result = rewardAPILogs.stream()
				.peek(lpm -> {
						lpm.setShowDate(dateTimtUtil.formatDateToStr(lpm.getCreatetime(), "yyyy-MM-dd"));
						lpm.setShowTime(dateTimtUtil.formatDateToStr(lpm.getCreatetime(), "HH:mm:ss"));
					})
				.sorted(rewardAPILogSort(conditionsRequest.getSortField(), conditionsRequest.getSortOrder()))
				.skip(pageSize * (conditionsRequest.getPageIndex() - 1))
				.limit(pageSize)
				.collect(Collectors.toList());
			
			return new ResponseEntity<JSGridReturnData<RewardAPILog>>(JSGridResponse.getResponseData(result, rewardAPILogs.size()), HttpStatus.OK);
		} else {
			throw new QueryNoDataException("查無資料!!!", 404);
		}
	}

	/**
	 * 依rewardId查詢
	 * @param rewardId
	 * @return RewardAPILog
	 */
    @Override
	public RewardAPILog findByrewardId(Long rewardId) {
		return rewardAPILogRepository.findById(rewardId).get();
	}
	
	/**
	 * 排序欄位設置
	 * @param sortField
	 * @param sortOrder
	 * @return Comparator<RewardAPILog>
	 */
	private Comparator<RewardAPILog> rewardAPILogSort(String sortField, String sortOrder) {
		switch(sortField) {
		case "RewardIdnTpan":
			return sortOrder.equals("asc") ? 
					Comparator.comparing(RewardAPILog::getRewardIdnTpan) : 
						Comparator.comparing(RewardAPILog::getRewardIdnTpan).reversed();
		case "RewardSeqNo":
			return sortOrder.equals("asc") ? 
					Comparator.comparing(RewardAPILog::getRewardSeqNo) : 
						Comparator.comparing(RewardAPILog::getRewardSeqNo).reversed();
		case "RewardTxnTime":
			return sortOrder.equals("asc") ? 
					Comparator.comparing(RewardAPILog::getRewardTxnTime) : 
						Comparator.comparing(RewardAPILog::getRewardTxnTime).reversed();
		}
		
		return sortOrder.equals("asc") ? 
				Comparator.comparing(RewardAPILog::getRewardId) : 
					Comparator.comparing(RewardAPILog::getRewardId).reversed();
	}
}

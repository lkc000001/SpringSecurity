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
import com.springsecuritydemo.entity.RewardAPILog;
import com.springsecuritydemo.entity.request.ConditionsRequest;
import com.springsecuritydemo.entity.response.JSGridResponse;
import com.springsecuritydemo.entity.response.JSGridReturnData;
import com.springsecuritydemo.exception.QueryNoDataException;
import com.springsecuritydemo.repositories.RewardApiLogRepository;
import com.springsecuritydemo.service.RewardApiLogService;
import com.springsecuritydemo.service.ServiceUtil;

@DS("DBTREWARD")
@Service
public class RewardApiLogServiceImpl implements RewardApiLogService {

	@Autowired
	private RewardApiLogRepository rewardAPILogRepository;
	
	@Autowired
	private ServiceUtil serviceUtil;
	
	/**
	 * RewardAPILog Table依查詢條件查詢,依分頁及排序Response
	 * @param ConditionsRequest
	 * @return ResponseEntity
	 * @throws ParseException 
	 */
    @Override
	public ResponseEntity<JSGridReturnData<RewardAPILog>> queryRewardAPILog(ConditionsRequest conditionsRequest) throws ParseException
	{
		serviceUtil.requestDateCheck(conditionsRequest);
		
		List<RewardAPILog> rewardAPILogs = rewardAPILogRepository.queryRewardAPILog(conditionsRequest.getQueryUserId(), conditionsRequest.getStartDate(), 
				conditionsRequest.getEndDate(), conditionsRequest.getQueryData(), conditionsRequest.getQueryType(), 
				conditionsRequest.getQueryUrl(), conditionsRequest.getPageIndex(), conditionsRequest.getPageSize());
		if(rewardAPILogs.size() > 0 ) {
		//排序
			List<RewardAPILog> result = rewardAPILogs.stream()
					.sorted(rewardAPILogSort(conditionsRequest.getSortField(), conditionsRequest.getSortOrder()))
					.collect(Collectors.toList());
		
			return new ResponseEntity<>(JSGridResponse.getResponseData(result, rewardAPILogRepository.count()), HttpStatus.OK);
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
		return serviceUtil.checkDataIsPresent(rewardAPILogRepository.findById(rewardId));
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
		default:
			return sortOrder.equals("asc") ? 
				Comparator.comparing(RewardAPILog::getRewardId) : 
					Comparator.comparing(RewardAPILog::getRewardId).reversed();
		}
	}
}

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
import com.springsecuritydemo.entity.UserTrackLog;
import com.springsecuritydemo.entity.request.ConditionsRequest;
import com.springsecuritydemo.entity.response.JSGridResponse;
import com.springsecuritydemo.entity.response.JSGridReturnData;
import com.springsecuritydemo.exception.QueryNoDataException;
import com.springsecuritydemo.repositories.UserTrackLogRepository;
import com.springsecuritydemo.service.ServiceUtil;
import com.springsecuritydemo.service.UserTrackLogService;
import com.springsecuritydemo.util.ValidateUtil;

@DS("DBAPIGL")
@Service
public class UserTrackLogServiceImpl implements UserTrackLogService {

	@Autowired
	private UserTrackLogRepository userTrackLogRepository;
	
	@Autowired
	private ValidateUtil validateUtil;
	
	@Autowired
	private ServiceUtil serviceUtil;
	
	/**
	 * UserTrackLog Table依查詢條件查詢,依分頁及排序Response
	 * @param conditionsRequest
	 * @return ResponseEntity
	 * @throws ParseException 
	 */
    @Override
	public ResponseEntity<JSGridReturnData<UserTrackLog>> queryUserTrackLog(ConditionsRequest conditionsRequest) throws ParseException
	{
		serviceUtil.requestDateCheck(conditionsRequest);
		Integer accountID = validateUtil.isBlank(conditionsRequest.getQueryUserId()) ? null : Integer.valueOf(conditionsRequest.getQueryUserId());
		List<UserTrackLog> userTrackLogs = userTrackLogRepository.queryUserTrackLog(accountID, conditionsRequest.getStartDate(), 
				 							conditionsRequest.getEndDate(), conditionsRequest.getQueryData(), conditionsRequest.getQueryUrl(),
				 							conditionsRequest.getPageIndex(), conditionsRequest.getPageSize());
		
		//排序
		if(userTrackLogs.size() > 0 ) {
			List<UserTrackLog> result = userTrackLogs.stream()
				.sorted(userTrackLogSort(conditionsRequest.getSortField(), conditionsRequest.getSortOrder()))
				.collect(Collectors.toList());
		
			return new ResponseEntity<>(JSGridResponse.getResponseData(result, userTrackLogRepository.count()), HttpStatus.OK);
		} else {
			throw new QueryNoDataException("查無資料!!!", 404);
		}
	}

	/**
	 * 依UserTrackLog查詢
	 * @param userTrackId
	 * @return UserTrackLog
	 */
    @Override
	public UserTrackLog findByUserTrackId(Long userTrackId) {
		return serviceUtil.checkDataIsPresent(userTrackLogRepository.findById(userTrackId));
	}
	
    /**
     * 儲存UserTrackLOG
     * @param ApiGuiLog
     */
	@Override
	public void save(UserTrackLog userTrackLog) {
		userTrackLogRepository.save(userTrackLog);
	}
	
	/**
	 * 排序欄位設置
	 * @param sortField
	 * @param sortOrder
	 * @return Comparator<UserTrackLog>
	 */
	private Comparator<UserTrackLog> userTrackLogSort(String sortField, String sortOrder) {
		switch(sortField) {
		case "UserTrackAccountID":
			return sortOrder.equals("asc") ? 
					Comparator.comparing(UserTrackLog::getUserTrackAccountID) : 
						Comparator.comparing(UserTrackLog::getUserTrackAccountID).reversed();
		case "UserTrackName":
			return sortOrder.equals("asc") ? 
					Comparator.comparing(UserTrackLog::getUserTrackName) : 
						Comparator.comparing(UserTrackLog::getUserTrackName).reversed();
		case "UserTrackUrl":
			return sortOrder.equals("asc") ? 
					Comparator.comparing(UserTrackLog::getUserTrackUrl) : 
						Comparator.comparing(UserTrackLog::getUserTrackUrl).reversed();
		default:
			return sortOrder.equals("asc") ? 
				Comparator.comparing(UserTrackLog::getUserTrackId) : 
					Comparator.comparing(UserTrackLog::getUserTrackId).reversed();
		}
	}
}

package com.springSecurityDemo.service.impl;

import java.text.ParseException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.springSecurityDemo.entity.UserTrackLog;
import com.springSecurityDemo.entity.request.ConditionsRequest;
import com.springSecurityDemo.entity.response.JSGridResponse;
import com.springSecurityDemo.entity.response.JSGridReturnData;
import com.springSecurityDemo.exception.QueryNoDataException;
import com.springSecurityDemo.repositories.UserTrackLogRepository;
import com.springSecurityDemo.service.ServiceUtil;
import com.springSecurityDemo.service.UserTrackLogService;
import com.springSecurityDemo.util.DateTimtUtil;
import com.springSecurityDemo.util.ValidateUtil;
import com.baomidou.dynamic.datasource.annotation.DS;

@DS("DBAPIGL")
@Service
public class UserTrackLogServiceImpl implements UserTrackLogService {

	@Autowired
	UserTrackLogRepository userTrackLogRepository;
	
	@Autowired
	DateTimtUtil dateTimtUtil;
	
	@Autowired
	ValidateUtil validateUtil;
	
	@Autowired
	ServiceUtil serviceUtil;
	
	/**
	 * UserTrackLog Table依查詢條件查詢,依分頁及排序Response
	 * @param conditionsRequest
	 * @return ResponseEntity
	 * @throws ParseException 
	 */
    @Override
	public ResponseEntity<?> queryUserTrackLog(ConditionsRequest conditionsRequest) throws ParseException
	{
		serviceUtil.requestDateCheck(conditionsRequest);
		Integer accountID = validateUtil.isBlank(conditionsRequest.getQueryUserId()) ? null : Integer.valueOf(conditionsRequest.getQueryUserId());
		List<UserTrackLog> userTrackLogs = userTrackLogRepository.queryUserTrackLog(accountID, conditionsRequest.getStartDate(), 
				 							conditionsRequest.getEndDate(), conditionsRequest.getQueryData(), conditionsRequest.getQueryUrl());
		//分頁,排序
		if(userTrackLogs.size() > 0 ) {
			int pageSize = conditionsRequest.getPageSize();
			List<UserTrackLog> result = userTrackLogs.stream()
				.peek(lpm -> {
						lpm.setShowDate(dateTimtUtil.formatDateToStr(lpm.getCreatetime(), "yyyy-MM-dd"));
						lpm.setShowTime(dateTimtUtil.formatDateToStr(lpm.getCreatetime(), "HH:mm:ss"));
					})
				.sorted(userTrackLogSort(conditionsRequest.getSortField(), conditionsRequest.getSortOrder()))
				.skip(pageSize * (conditionsRequest.getPageIndex() - 1))
				.limit(pageSize)
				.collect(Collectors.toList());
		
			return new ResponseEntity<JSGridReturnData<UserTrackLog>>(JSGridResponse.getResponseData(result, userTrackLogs.size()), HttpStatus.OK);
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
		return userTrackLogRepository.findById(userTrackId).get();
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
		}
		
		return sortOrder.equals("asc") ? 
				Comparator.comparing(UserTrackLog::getUserTrackId) : 
					Comparator.comparing(UserTrackLog::getUserTrackId).reversed();
	}
}

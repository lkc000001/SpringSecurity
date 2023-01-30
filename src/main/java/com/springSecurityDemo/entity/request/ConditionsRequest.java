package com.springsecuritydemo.entity.request;

import java.util.Date;

import lombok.Data;

public @Data class ConditionsRequest {

	private Long queryId;
	
	private String queryUserId;
	
	private String queryData;
	
	private String queryDate;
	
	private String queryType;
	
	private String queryUrl;
	
	private String timeStart;
	
	private String timeEnd;
	
	private Date startDate;
	
	private Date endDate;
	
	private String queryTable;
	
	
	private Integer pageIndex;
	
	private Integer pageSize;
	
	private String sortField;  
	
	private String sortOrder; 
	
}

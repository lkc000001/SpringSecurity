package com.springSecurityDemo.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.springSecurityDemo.entity.SmsOtpLog;

public interface SmsOtpLogRepository extends PagingAndSortingRepository<SmsOtpLog, Long>{

	@Query("SELECT * " +
	 		"FROM SmsOtpLog " +
	 		"WHERE ((:userId IS NULL OR :userId = '') OR USERID=:userId) " +
			"  AND ((:startDate IS NULL) OR CREATETIME BETWEEN :startDate AND :endDate) " + 
			"  AND ((:msg IS NULL OR :msg = '') OR MSG LIKE :msg) " + 
			"  AND ((:queryType IS NULL OR :queryType = '') OR STATUSCODE LIKE :queryType) " + 
			"  AND ((:queryUrl IS NULL OR :queryUrl = '') OR SYSFLAG LIKE :queryUrl) ")
	List<SmsOtpLog> querySmsOtpLog(@Param("userId") String userId, @Param("startDate") Date startDate, 
								 @Param("endDate") Date endDate, @Param("msg") String msg,
								 @Param("queryType") String queryType, @Param("queryUrl") String queryUrl);
}

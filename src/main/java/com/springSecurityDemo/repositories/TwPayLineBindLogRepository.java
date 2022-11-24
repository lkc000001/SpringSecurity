package com.springSecurityDemo.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.springSecurityDemo.entity.TwPayLineBindLog;


public interface TwPayLineBindLogRepository extends PagingAndSortingRepository<TwPayLineBindLog, Long>{
	
	@Query("SELECT * " +
	 		"FROM TWPayLineBindLog " +
	 		"WHERE ((:startDate IS NULL) OR CREATETIME BETWEEN :startDate AND :endDate) " + 
			"  AND ((:TwPayLineBindLogData IS NULL OR :TwPayLineBindLogData = '') OR TWPayLineBindLogData LIKE :TwPayLineBindLogData) "+ 
			"  AND ((:queryType IS NULL OR :queryType = '') OR twPayLineBindLogSendStatus LIKE :queryType) " + 
			"  AND ((:queryUrl IS NULL OR :queryUrl = '') OR twPayLineBindLogUrl LIKE :queryUrl) ")
	List<TwPayLineBindLog> querytwPayLineBindLog(@Param("TWPayLineBindLogLineUid") Long TWPayLineBindLogLineUid, @Param("startDate") Date startDate, 
								 @Param("endDate") Date endDate, @Param("TwPayLineBindLogData") String TwPayLineBindLogData,
								 @Param("queryType") String queryType, @Param("queryUrl") String queryUrl);

}

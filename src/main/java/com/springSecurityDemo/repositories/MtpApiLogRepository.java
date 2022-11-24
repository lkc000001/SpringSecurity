package com.springSecurityDemo.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.springSecurityDemo.entity.MtpApiLog;

public interface MtpApiLogRepository extends PagingAndSortingRepository<MtpApiLog, Long>{

	@Query("SELECT * " +
	 		"FROM MTPAPILog " +
	 		"WHERE ((:mtpMobilePhone IS NULL OR :mtpMobilePhone = '') OR MTPMobilePhone=:mtpMobilePhone) " +
			"  AND ((:startDate IS NULL) OR CREATETIME BETWEEN :startDate AND :endDate) " + 
			"  AND ((:mtpData IS NULL OR :mtpData = '') OR MTPDATA LIKE :mtpData) " + 
			"  AND ((:queryType IS NULL OR :queryType = '') OR MTPSendStatus LIKE :queryType) " + 
			"  AND ((:queryUrl IS NULL OR :queryUrl = '') OR MTPUrl LIKE :queryUrl) ")
	List<MtpApiLog> queryMtpApiLog(@Param("mtpMobilePhone") String mtpMobilePhone, @Param("startDate") Date startDate, 
								 @Param("endDate") Date endDate, @Param("mtpData") String mtpData,
								 @Param("queryType") String queryType, @Param("queryUrl") String queryUrl);

}

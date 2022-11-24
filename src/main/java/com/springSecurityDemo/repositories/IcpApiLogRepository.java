package com.springSecurityDemo.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.springSecurityDemo.entity.IcpApiLog;

public interface IcpApiLogRepository extends PagingAndSortingRepository<IcpApiLog, Long>{

	@Query("SELECT * " +
	 		"FROM ICPAPILog " +
	 		"WHERE ((:icpCustId IS NULL OR :icpCustId = '') OR ICPCustId=:icpCustId) " +
			"  AND ((:startDate IS NULL) OR CREATETIME BETWEEN :startDate AND :endDate) " + 
			"  AND ((:icpData IS NULL OR :icpData = '') OR ICPData LIKE :icpData) " + 
			"  AND ((:queryType IS NULL OR :queryType = '') OR ICPType LIKE :queryType) " + 
			"  AND ((:queryUrl IS NULL OR :queryUrl = '') OR ICPURL LIKE :queryUrl) ")
	List<IcpApiLog> queryIcpApiLog(@Param("icpCustId") String icpCustId, @Param("startDate") Date startDate, 
								 @Param("endDate") Date endDate, @Param("icpData") String icpData,
								 @Param("queryType") String queryType, @Param("queryUrl") String queryUrl);

}

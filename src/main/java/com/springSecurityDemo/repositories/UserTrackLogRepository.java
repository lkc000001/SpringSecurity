package com.springSecurityDemo.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.springSecurityDemo.entity.UserTrackLog;

public interface UserTrackLogRepository extends PagingAndSortingRepository<UserTrackLog, Long>{

	@Query("SELECT * " +
	 		"FROM USERTRACKLog " +
	 		"WHERE (:accountId IS NULL OR UserTrackAccountID=:accountId) " +
			"  AND ((:startDate IS NULL) OR CREATETIME BETWEEN :startDate AND :endDate) " + 
			"  AND ((:userTrackData IS NULL OR :userTrackData = '') OR UserTrackData LIKE :userTrackData) " + 
			"  AND ((:queryUrl IS NULL OR :queryUrl = '') OR UserTrackUrl LIKE :queryUrl) ")
	List<UserTrackLog> queryUserTrackLog(@Param("accountId") Integer accountId, @Param("startDate") Date startDate, 
								 @Param("endDate") Date endDate, @Param("userTrackData") String userTrackData,
								 @Param("queryUrl") String queryUrl);

}

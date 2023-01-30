package com.springsecuritydemo.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.springsecuritydemo.entity.ApiGuiLog;

public interface ApiGuiLogRepository extends CrudRepository<ApiGuiLog, Long>{

	@Query("SELECT *, convert(char(10), createtime, 120) AS showDate, convert(char(10), createtime, 108) AS showTime, COUNT(1) OVER() AS totalCount " +
	 		"FROM APIGUILog " +
	 		"WHERE ((:apiGuiUserId IS NULL OR :apiGuiUserId = '') OR ApiGuiUserId LIKE :apiGuiUserId) " +
			"  AND ((:startDate IS NULL) OR CREATETIME BETWEEN :startDate AND :endDate) " + 
			"  AND ((:apiGuiData IS NULL OR :apiGuiData = '') OR ApiGuiData LIKE :apiGuiData) " + 
			"  AND ((:apiGuiSendStatus IS NULL OR :apiGuiSendStatus = '') OR ApiGuiSendStatus LIKE :apiGuiSendStatus) " + 
			"  AND ((:apiGuiUrl IS NULL OR :apiGuiUrl = '') OR ApiGuiUrl LIKE :apiGuiUrl) " + 
			"  ORDER BY ApiGuiId DESC " + 
			"  OFFSET (:PageIndex -1) * :PageSize ROWS " + 
			"  FETCH NEXT :PageSize ROWS ONLY")
	List<ApiGuiLog> queryApiGuiLog(@Param("apiGuiUserId") String apiGuiUserId, @Param("startDate") Date startDate, 
								 @Param("endDate") Date endDate, @Param("apiGuiData") String apiGuiData,
								 @Param("apiGuiSendStatus") String apiGuiSendStatus, @Param("apiGuiUrl") String apiGuiUrl, 
								 @Param("PageIndex") Integer pageIndex, @Param("PageSize") Integer pageSize);
	
	long count();
}

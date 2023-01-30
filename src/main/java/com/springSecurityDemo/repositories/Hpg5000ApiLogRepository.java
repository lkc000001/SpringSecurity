package com.springsecuritydemo.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.springsecuritydemo.entity.Hpg5000ApiLog;

public interface Hpg5000ApiLogRepository extends PagingAndSortingRepository<Hpg5000ApiLog, Long>{

	@Query("SELECT *, convert(char(10), createtime, 120) AS showDate, convert(char(10), createtime, 108) AS showTime " +
	 		"FROM HPG5000APILog " +
	 		"WHERE ((:hpgLogIdnTxn IS NULL OR :hpgLogIdnTxn = '') OR HPGLogIdnTxn LIKE :hpgLogIdnTxn) " +
			"  AND ((:startDate IS NULL) OR CREATETIME BETWEEN :startDate AND :endDate) " + 
			"  AND ((:hpgData IS NULL OR :hpgData = '') OR HPGData LIKE :hpgData) " + 
			"  AND ((:queryType IS NULL OR :queryType = '') OR HPGLogType LIKE :queryType) " + 
			"  AND ((:queryUrl IS NULL OR :queryUrl = '') OR HPGLogURL LIKE :queryUrl) "  + 
			"  ORDER BY HPGLogId DESC " + 
			"  OFFSET (:PageIndex -1) * :PageSize ROWS " + 
			"  FETCH NEXT :PageSize ROWS ONLY")
	List<Hpg5000ApiLog> queryHpg5000ApiLog(@Param("hpgLogIdnTxn") String hpgLogIdnTxn, @Param("startDate") Date startDate, 
								 @Param("endDate") Date endDate, @Param("hpgData") String hpgData,
								 @Param("queryType") String queryType, @Param("queryUrl") String queryUrl, 
								 @Param("PageIndex") Integer pageIndex, @Param("PageSize") Integer pageSize);

	long count(); 
}

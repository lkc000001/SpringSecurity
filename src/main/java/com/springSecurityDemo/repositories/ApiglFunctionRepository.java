package com.springsecuritydemo.repositories;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.springsecuritydemo.entity.ApiGLFunction;

public interface ApiglFunctionRepository extends PagingAndSortingRepository<ApiGLFunction, Long>{

	@Query("SELECT * " +
	 		"FROM APIGLFUNCTION " +
	 		"WHERE ((:apiglFunctionName IS NULL OR :apiglFunctionName = '') OR apiglfunctionname LIKE :apiglFunctionName) " +
			"  AND ((:apiglFunctionShowName IS NULL OR :apiglFunctionShowName = '') OR apiglfunctionshowname LIKE :apiglFunctionShowName)" + 
			"  AND ((:apiglFunctionUrl IS NULL OR :apiglFunctionUrl = '') OR apiglfunctionurl LIKE :apiglFunctionUrl)" + 
			"  AND ((:type IS NULL OR :type = '') OR type=:type) " + 
			"  AND ((:enabled IS NULL OR :enabled = '') OR enabled=:enabled) " + 
			"  ORDER BY apiglfunctionid DESC " + 
			"  OFFSET (:PageIndex -1) * :PageSize ROWS " + 
			"  FETCH NEXT :PageSize ROWS ONLY")
	List<ApiGLFunction> queryApiglFunction(@Param("apiglFunctionName") String apiglFunctionName, @Param("apiglFunctionShowName") String apiglFunctionShowName, 
								 @Param("apiglFunctionUrl") String apiglFunctionUrl, @Param("enabled") String enabled, @Param("type") String type, 
								 @Param("PageIndex") Integer pageIndex, @Param("PageSize") Integer pageSize);
	
	List<ApiGLFunction> findAll();
	
	ApiGLFunction findByApiglFunctionName(String apiglFunctionName);
	
	@Query("SELECT * " +
	 		"FROM APIGLFUNCTION " +
	 		"WHERE enabled=1")
	List<ApiGLFunction> findByIsEnable();
}

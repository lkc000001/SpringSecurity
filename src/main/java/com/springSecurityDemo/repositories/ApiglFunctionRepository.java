package com.springSecurityDemo.repositories;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.springSecurityDemo.entity.ApiGLFunction;

public interface ApiglFunctionRepository extends CrudRepository<ApiGLFunction, Long>{

	@Query("SELECT * " +
	 		"FROM APIGLFUNCTION " +
	 		"WHERE ((:apiglFunctionName IS NULL OR :apiglFunctionName = '') OR apiglfunctionname LIKE :apiglFunctionName) " +
			"  AND ((:apiglFunctionShowName IS NULL OR :apiglFunctionShowName = '') OR apiglfunctionshowname LIKE :apiglFunctionShowName)" + 
			"  AND ((:apiglFunctionUrl IS NULL OR :apiglFunctionUrl = '') OR apiglfunctionurl LIKE :apiglFunctionUrl)" + 
			"  AND ((:type IS NULL OR :type = '') OR type=:type) " + 
			"  AND ((:enabled IS NULL OR :enabled = '') OR enabled=:enabled) ")
	List<ApiGLFunction> queryApiglFunction(@Param("apiglFunctionName") String apiglFunctionName, @Param("apiglFunctionShowName") String apiglFunctionShowName, 
								 @Param("apiglFunctionUrl") String apiglFunctionUrl, @Param("enabled") String enabled, @Param("type") String type);

	List<ApiGLFunction> findAll();
	
	ApiGLFunction findByApiglFunctionName(String apiglFunctionName);
}

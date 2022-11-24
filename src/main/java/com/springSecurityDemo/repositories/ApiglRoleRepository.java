package com.springSecurityDemo.repositories;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.springSecurityDemo.entity.ApiGLRole;


public interface ApiglRoleRepository extends CrudRepository<ApiGLRole, Long>{

	@Query("SELECT * " +
	 		"FROM APIGLROLE " +
	 		"WHERE ((:apiglRoleNumber IS NULL OR :apiglRoleNumber = '') OR apiglrolenumber=:apiglRoleNumber) " +
			"  AND ((:apiglRoleName IS NULL OR :apiglRoleName = '') OR apiglrolename=:apiglRoleName) " + 
			"  AND ((:apiglRoleType IS NULL OR :apiglRoleType = '') OR apiglroletype=:apiglRoleType) " + 
			"  AND ((:apiglRoleDirections IS NULL OR :apiglRoleDirections = '') OR apiglroledirections LIKE :apiglRoleDirections) " + 
			"  AND ((:enabled IS NULL OR :enabled = '') OR enabled=:enabled) ")
	List<ApiGLRole> queryApiglRole(@Param("apiglRoleNumber") String apiglRoleNumber, @Param("apiglRoleName") String apiglRoleName, 
			 @Param("apiglRoleType") String apiglRoleType, @Param("apiglRoleDirections") String apiglRoleDirections, @Param("enabled") String enabled);
	
	ApiGLRole findByApiglRoleNameAndApiglRoleNumberAndApiglRoleType(String apiglrolename, String apiglrolenumber, String apiglRoleType);

}

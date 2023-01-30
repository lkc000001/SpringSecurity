package com.springsecuritydemo.repositories;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.springsecuritydemo.entity.AppUser;

public interface UserRepository extends CrudRepository<AppUser, Long>{

	@Query("SELECT * " +
	 		"FROM [USER] " +
	 		"WHERE ((:branch IS NULL OR :branch = '') OR branch=:branch) " +
			"  AND ((:groupName IS NULL OR :groupName = '') OR groupname=:groupName) " + 
			"  AND (:accountId IS NULL OR accountid=:accountId) " + 
			"  AND ((:account IS NULL OR :account = '') OR account=:account) " + 
			"  AND ((:name IS NULL OR :name = '') OR name LIKE :name) " + 
			"  AND ((:enabled IS NULL OR :enabled = '') OR enabled=:enabled)")
	List<AppUser> queryUserPermission(@Param("branch") String branch, @Param("groupName") String groupName, 
			 @Param("accountId") Integer accountId, @Param("account") String account, 
			 @Param("name") String name, @Param("enabled") String enabled);
	
	AppUser findByAccount(String account);
	
	List<AppUser> findAll();
	
	@Query("SELECT * " +
	 		"FROM [USER]" +
	 		"WHERE account=:account AND enabled=1")
	AppUser findByisEnable(String account);
	
}

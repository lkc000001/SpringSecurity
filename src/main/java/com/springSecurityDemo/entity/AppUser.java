package com.springsecuritydemo.entity;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.springsecuritydemo.entity.request.JSGridFilter;

import lombok.Data;

@Table("[USER]")	
public @Data class AppUser extends JSGridFilter implements Serializable {
	
	@Id
	@Column("userid")
	private Long userId;
	
	@Column("branch")
	private String branch;
	
	@Column("groupname")
	private String groupName;
	
	@Column("accountid")
	private Integer accountId;
	
	@Column("account")
	private String account;
	
	@Column("name")
	private String name;
	
	@Column("pwd")
	private String pwd;

	@Column("enabled")
	private String enabled;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "GMT+8")
	@Column("createtime")
	private Date createTime;
	
	@Column("createuser")
	private String createUser;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "GMT+8")
	@Column("updatetime")
	private Date updateTime;
	
	@Column("updateuser")
	private String updateUser;
	
	@Transient
	private String usercode;
	
	@Transient
	private String authcode;
	
	@Transient
	private String shapwd;

	public AppUser(String branch, String groupName, Integer accountId, String account, String name, String enabled) {
		super();
		this.branch = branch;
		this.groupName = groupName;
		this.accountId = accountId;
		this.account = account;
		this.name = name;
		this.enabled = enabled;
	}

	public AppUser() {
		
	}
	
}

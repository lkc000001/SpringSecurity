package com.springSecurityDemo.entity;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.springSecurityDemo.entity.request.JSGridFilter;

import lombok.Data;

@Table("MTPAPILog")
public @Data class MtpApiLog extends JSGridFilter implements Serializable {
	
	@Id
	@Column("MTPLID")
	private Long mtpLId;
	
	@Column("MTPMobilePhone")
	private Integer mtpMobilePhone;
	
	@Column("MTPBankCode")
	private String mtpBankCode;
	
	@Column("MTPToken")
	private String mtpToken;
	
	@Column("MTPNewToken")
	private String mtpNewToken;
	
	@Column("MTPRCode")
	private String mtpRCode;
	
	@Column("MTPDATA")
	private String mtpData;
	
	@Column("MTPUrl")
	private String mtpUrl;
	
	@Column("MTPSendStatus")
	private String mtpSendStatus;
	
	@Column("CREATETIME")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "GMT+8")
	private Date createtime;	
}

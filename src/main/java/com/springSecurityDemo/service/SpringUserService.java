package com.springsecuritydemo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.springsecuritydemo.entity.AppUser;
import com.springsecuritydemo.repositories.UserRepository;
import com.springsecuritydemo.util.ValidateUtil;

import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

public class SpringUserService implements UserDetailsService {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	ValidateUtil validateUtil;
	
    @Override
    public UserDetails loadUserByUsername(String username) {
    	
    	AppUser user = userRepository.findByAccount(username);
    	String pwd = "";
    	String authority = "";
    	
    	if(validateUtil.isEmpty(user) || validateUtil.isBlank(user.getPwd())) {
    		throw new InternalAuthenticationServiceException("登入失敗，請確認帳號及密碼是否正確");
    	} else {
    		pwd = user.getPwd();
    		authority = user.getGroupName();
    	}
    	return new User(username, pwd,  AuthorityUtils.commaSeparatedStringToAuthorityList(authority));
    }
}

package com.springSecurityDemo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.springSecurityDemo.util.ValidateUtil;
import com.springSecurityDemo.entity.AppUser;
import com.springSecurityDemo.repositories.UserRepository;

import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

@Service
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
    	
    	if(validateUtil.isEmpty(user)) {
    		throw new InternalAuthenticationServiceException("登入失敗，請確認帳號及密碼是否正確");
    	} else {
    		pwd = user.getPwd();
    		authority = user.getGroupName();
    		
    	}
    	return new User(username, pwd,  AuthorityUtils.commaSeparatedStringToAuthorityList(authority));
    }
}

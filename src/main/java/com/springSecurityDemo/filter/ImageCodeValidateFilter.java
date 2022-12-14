package com.springSecurityDemo.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import com.springSecurityDemo.controller.ValidateCodeController;
import com.springSecurityDemo.entity.request.ImageCode;
import com.springSecurityDemo.exception.AuthCodeException;
import com.springSecurityDemo.exception.ValidateCodeException;
import com.springSecurityDemo.util.ValidateUtil;

@Component
public class ImageCodeValidateFilter extends OncePerRequestFilter {

	//private String codeParamter = "authCode"; 
	
	private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();
	    
	@Autowired
	ValidateUtil validateUtil;
	
	 /*@Override
	    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
	        if (!request.getMethod().equals("POST")) {
	            throw new AuthenticationServiceException(
	                    "Authentication method not supported: " + request.getMethod());
	        }
	        String usercode = request.getParameter("usercode");
	        System.out.println(usercode);
	        String sessioncode = (String) request.getSession().getAttribute("authCode");
	        System.out.println(sessioncode);
	        if (!validateUtil.isEmpty(usercode) && !validateUtil.isEmpty(sessioncode) && usercode.equalsIgnoreCase(sessioncode)) {
	            return super.attemptAuthentication(request, response);
	        }
	        throw new AuthCodeException("???????????????", 400);
	    }*/
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		//System.out.println("URL: "+request.getRequestURI());
		//System.out.println("Method: "+request.getMethod());
		if (request.getRequestURI().contains("/loginAction")
                && request.getMethod().equals("POST")) {
            try {
                validateCode(new ServletWebRequest(request));
            } catch (ValidateCodeException e) {
            	throw new InternalAuthenticationServiceException(e.getMessage());
            	/*new AuthenticationFailureHandler() {
            		@Override
            		public void onAuthenticationFailure(HttpServletRequest httpServletRequest,
            				HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException {
            			 //request.getSession().setAttribute("SPRING_SECURITY_LAST_EXCEPTION", e.getMessage());
            			 //response.sendRedirect(request.getContextPath() + "/login/?error=true");
            			throw new ValidateCodeException(e.getMessage());
            		}
            	}.onAuthenticationFailure(request, response, e);*/
            	//throw new InternalAuthenticationServiceException(e.getMessage());
                //authenticationFailureHandler.onAuthenticationFailure(request, response, e);
                //return;
            }
        }
        filterChain.doFilter(request, response);
		
	}
	
	private void validateCode(ServletWebRequest servletWebRequest) throws ServletRequestBindingException {
		ImageCode codeInSession = (ImageCode) sessionStrategy.getAttribute(servletWebRequest, ValidateCodeController.SESSION_KEY);
		String codeInRequest = ServletRequestUtils.getStringParameter(servletWebRequest.getRequest(), "imageCode");
		//System.out.println("codeInSession"+codeInSession.getCode());
		//System.out.println("codeInRequest"+codeInRequest);
	        //String requestCode = request.getParameter("usercode");
	        /*System.out.println(requestCode);
	        if(requestCode == null) {
	            requestCode = "";
	        }
	        requestCode = requestCode.trim();

	        HttpSession session = request.getSession();

	        String savedCode = (String) session.getAttribute("authCode");*/
	        /*if (savedCode != null) {
	            session.removeAttribute("authCode");
	        }*/

	        if (validateUtil.isBlank(codeInRequest)) {
	            throw new AuthCodeException("?????????????????????", 400);
	        }

	        if (validateUtil.isEmpty(codeInSession)) {
	            throw new ValidateCodeException("??????????????????");
	        }
	        
	        if (codeInSession.isExpire()) {
	            sessionStrategy.removeAttribute(servletWebRequest, ValidateCodeController.SESSION_KEY);
	            throw new ValidateCodeException("??????????????????");
	        }

	        if (!codeInSession.getCode().equals(codeInRequest)) {
	            throw new ValidateCodeException("???????????????");
	        }
	        sessionStrategy.removeAttribute(servletWebRequest, ValidateCodeController.SESSION_KEY);
	    }
}

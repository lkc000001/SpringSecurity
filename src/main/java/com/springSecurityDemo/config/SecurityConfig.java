package com.springsecuritydemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.springsecuritydemo.filter.ImageCodeValidateFilter;
import com.springsecuritydemo.service.SpringUserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public AuthenticationSuccessHandler myAuthenticationSuccessHandler(){
        return new MySimpleUrlAuthenticationSuccessHandler();
    }
    
    @Bean
    public SpringUserService springUserService() {
        return new SpringUserService();
    }
    
    @Bean
    public ImageCodeValidateFilter imageCodeValidateFilter() {
        return new ImageCodeValidateFilter();
    }
    
	private final String[] unAuthedUrls = new String[]{
            "/","/login/**","/code/image","/CSS/**"
    };
	
	/**
    * 不需要驗證的URL
    */
    @Bean
    public WebSecurityCustomizer ignoringCustomizer() {
        return web-> web
                .ignoring()
                .antMatchers(unAuthedUrls);
    }
     
     /**
      * 配置Filter
      */
     @Bean
     SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    	 http.formLogin()
			 .loginProcessingUrl("/loginAction")
			 .loginPage("/login/")
			 .successHandler(myAuthenticationSuccessHandler())
			 .failureUrl("/login/?error=true");
    	 
    	 http.addFilterBefore(imageCodeValidateFilter(), UsernamePasswordAuthenticationFilter.class);
    	 
    	 http//.addFilterBefore(imageCodeValidateFilter, UsernamePasswordAuthenticationFilter.class)
     				//.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
     				.authorizeRequests()
     				.anyRequest().authenticated() // 所有請求都要驗證
     				//.antMatchers("/lpmApiLog/**").authenticated()
     				//.antMatchers("**").permitAll()
     				//.anyRequest().authenticated()
    	 			.and()
     				.userDetailsService(springUserService())
     				.csrf().disable();
    	 
    	 http.logout()
    	 	 .logoutUrl("/logout")
			 .deleteCookies("JSESSIONID")
			 .logoutSuccessUrl("/login/");
			 //.logoutRequestMatcher(new AntPathRequestMatcher("/logout"));
    	 
    	 return http.build();
     }
}

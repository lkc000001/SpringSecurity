package com.springSecurityDemo.config;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.springSecurityDemo.filter.ImageCodeValidateFilter;
import com.springSecurityDemo.service.SpringUserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Autowired
    private SpringUserService springUserService;
	
	@Autowired
	private ImageCodeValidateFilter imageCodeValidateFilter;
	
	private final String[] unAuthedUrls = new String[]{
            "/","/login/**","/code/image"
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
    	 
    	 http.addFilterBefore(imageCodeValidateFilter, UsernamePasswordAuthenticationFilter.class);
    	 
    	 http//.addFilterBefore(imageCodeValidateFilter, UsernamePasswordAuthenticationFilter.class)
     				//.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
     				.authorizeRequests()
     				.antMatchers("/lpmApiLog/**").authenticated()
     				//.antMatchers("**").permitAll()
     				//.anyRequest().authenticated()
    	 			.and()
     				.userDetailsService(springUserService)
     				.csrf().disable();
    	 
    	 http.logout()
			 .deleteCookies("JSESSIONID")
			 .logoutSuccessUrl("/login/");
			 //.logoutRequestMatcher(new AntPathRequestMatcher("/logout"));
    	 
    	 return http.build();
     }
     /*
      @Bean
      public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
          http.cors(Customizer.withDefaults())
                  .csrf().disable()
                  .authorizeRequests(auth -> auth.anyRequest().authenticated())
                  .authenticationManager(authenticationManager)
                  .addFilter(new JwtAuthorizationFilter(authenticationManager))
                  .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                  .and()
                  .exceptionHandling().authenticationEntryPoint(new UnAuthorizedExceptionEntryPoint())
                  .accessDeniedHandler(new JwtAccessDeniedHandler());
          return http.build();
      }*/
      
      @Bean
      public BCryptPasswordEncoder bCryptPasswordEncoder() {
          return new BCryptPasswordEncoder();
      }
      
      @Bean
      public AuthenticationSuccessHandler myAuthenticationSuccessHandler(){
          return new MySimpleUrlAuthenticationSuccessHandler();
      }
	/*@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.formLogin()
			.loginProcessingUrl("/login")
			.loginPage("/login/")
			.defaultSuccessUrl("/lpmApiLog/");
  	 //ImageCodeValidateFilter imageCodeValidateFilter = new ImageCodeValidateFilter();
  	 //http.addFilterBefore(imageCodeValidateFilter, UsernamePasswordAuthenticationFilter.class);
  	 
			http
   				//.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
   				//.and()
   				.authorizeRequests()
   				.antMatchers("/lpmApiLog/**").authenticated()
   				.antMatchers("/").permitAll()
   				.antMatchers("/login/**").permitAll()
   				.antMatchers("/code/image").permitAll()
   				//.antMatchers("**").permitAll()
   				.and()
   				.rememberMe()
   				.userDetailsService(userDetailsService)
   				.tokenValiditySeconds(60 * 60 * 24 *7)
  	 			.and()
   				.logout()
   				.deleteCookies("JSESSIONID")
   				.logoutSuccessUrl("/login/")
   				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
   				.and().csrf().disable();*/
  	 /*
	        http.authorizeRequests()
	        .antMatchers("/**").permitAll()
	        //.antMatchers("/login/", "/").permitAll()
	        //.anyRequest()
            //.authenticated()
            //.antMatchers("/**").permitAll()
	        //.antMatchers("/**").authenticated()
//	        .antMatchers("/hello2", "/add","/test", "/test2","/index","/aaa.html")
	                //.permitAll()
	        		//.anyRequest()
	                //.authenticated()
	                
	                .and()
	                .formLogin()
	                .loginPage("/login/")
	                .defaultSuccessUrl("/index.html")
//	                .permitAll()

//	                .httpBasic()
	                .and().csrf().disable();
	    }*/
	
}

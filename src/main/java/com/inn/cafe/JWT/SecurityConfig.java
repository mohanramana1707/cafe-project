package com.inn.cafe.JWT;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

@Configuration    // configuration class
@EnableWebSecurity
public class SecurityConfig {                // same as application config in JWT project
	
	@Autowired
	CustomUserDetailService customUserDetailService;
	
	
	@Autowired
	JwtFilter jwtFilter;
	
	 // addded
	// authentication based on CUSTOM User Details
//	protected void configure(AuthenticationManagerBuilder auth) throws Exception{  
//		auth.userDetailsService(customUserDetailService);    // kind of registering our custom Userdetails
//	}
//	
	
	@Bean  //1111111111111111111111
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {     
		return config.getAuthenticationManager();
		
	}
	
	//22222222222222222222222222222(added extra)
	@Bean
	public AuthenticationProvider authenticationProvider() {               // responsibe for fetch userdetails and encode PASSWORD
		
		DaoAuthenticationProvider authProvider= new DaoAuthenticationProvider();
		
		authProvider.setUserDetailsService(customUserDetailService);//333333333333333333333333333333
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}
	
//	@Override
//	@Bean
//	public AuthenticationManager authenticationManagerBean() throws Exception {
//		return super.authenticationManagerBean();
//	}

   
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		
		
		http
		.cors().configurationSource(request->new CorsConfiguration().applyPermitDefaultValues())
		   .and()
			.csrf()
			.disable()
			.authorizeHttpRequests()    // authorizing 
			.requestMatchers("/user/login","/user/signup","/user/forgotPassword")  //,"/user/signup"
			.permitAll()
			
			.anyRequest()    // authentication
			.authenticated()
			.and()
			.exceptionHandling()
			
			.and()
			.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		
		http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
				
		//it adds a filter named jwtFilter before the UsernamePasswordAuthenticationFilter class in the filter chain of the HTTP request.
		//The UsernamePasswordAuthenticationFilter is a built-in filter in Spring Security, a popular Java framework for securing web applications
		//, which handles user authentication using a username and password. By adding the JWT filter before this filter,
		//the application ensures that users are first authenticated using the JWT mechanism before attempting to authenticate using a traditional username and password.
		return http.build();
		
	}
	
	
}

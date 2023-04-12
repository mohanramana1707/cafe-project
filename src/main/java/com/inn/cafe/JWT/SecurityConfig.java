package com.inn.cafe.JWT;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
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
	protected void configure(AuthenticationManagerBuilder auth) throws Exception{  
		auth.userDetailsService(customUserDetailService);    // kind of registering our custom Userdetails
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {     
		return config.getAuthenticationManager();
		
	}
	
//	@Bean
//	@Override
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
			.authorizeHttpRequests()
			.requestMatchers("/user/login","/user/forgotPassword")  //,"/user/signup"
			.permitAll()
			
			.anyRequest()
			.authenticated()
			.and()
			.exceptionHandling()
			
			.and()
			.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		
		http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
				
			
		return http.build();
		
	}
	
	
}

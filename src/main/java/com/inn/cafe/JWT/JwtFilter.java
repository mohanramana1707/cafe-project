package com.inn.cafe.JWT;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;



// 1st class to executed when request is made in postman
@Component
@Slf4j
//@RequiredArgsConstructor //for private final
public class JwtFilter extends OncePerRequestFilter{

	
	@Autowired
	private JwtUtil jwtUtil ;
	
	@Autowired
	private CustomUserDetailService service;
	
	Claims claims=null;
	private String userName=null;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		log.error("number 1 : jwtFilter class ");
		
		// no token validation is required(by passing the given end points) , directly goes to servelet then to controller
		
		// GENERATE Token
		if(request.getServletPath().matches("/user/login | /user/signup ") ) {  //| /user/signup | /user/forgotPassword
			
			log.warn("inside IF ");
			
			filterChain.doFilter(request, response);  // goes to next filter
		}
		
		//token validation required
		
		// VALIDATE token
		else {
			log.warn("inside ELSE signup");
			
			final String authorizationHeader= request.getHeader("Authorization");
			final String token;
			
			// token should present with request, if not throw 403 forbidden
			if(authorizationHeader!=null && authorizationHeader.startsWith("Bearer ")) {
				
				log.warn("inside ELSE signup--- token is there");
				
				token=authorizationHeader.substring(7);
				userName=jwtUtil.extractUserName(token);
				claims=jwtUtil.extractAllClaims(token);
				
				log.warn("inside ELSE signup---got UserName and claims from JWT UTILS");
				
				// checking userName from token with principle(current user) , if there is no current user
				if(userName!=null && SecurityContextHolder.getContext().getAuthentication()==null) {
					
					log.warn("inside ELSE signup---got UserName from token and null auth");
					
					UserDetails userDetail= service.loadUserByUsername(userName);//CHECK DB if userName is present and  return type is authentication principle
					
					log.error("userDeatil got from  CUSTOM USERDETAIL class ");
					
					if(jwtUtil.isTokenValid(token, userDetail)) {
						
						UsernamePasswordAuthenticationToken authToken= new UsernamePasswordAuthenticationToken(userDetail, null, userDetail.getAuthorities());
						authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
						SecurityContextHolder.getContext().setAuthentication(authToken);
					}
				}	
				
			}
			filterChain.doFilter(request, response);
			
		}
		
	}
	
	public boolean isAdmin() {
		return "admin".equalsIgnoreCase((String) claims.get("role"));   // checking the token claims
	}
	
	public boolean isUser() {
		return "user".equalsIgnoreCase((String) claims.get("role"));
	}

	public String getCurrentUser() {
		return userName;
	}

}

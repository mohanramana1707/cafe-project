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

@Component
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
		
		// no token validation is required
		if(request.getServletPath().matches("/user/login  | /user/forgotPassword")) {  //| /user/signup
			filterChain.doFilter(request, response);
		}
		
		//token validation required
		else {
			
			String authorizationHeader= request.getHeader("Authorization");
			String token;
			
			if(authorizationHeader!=null && authorizationHeader.startsWith("Bearer ")) {
				
				token=authorizationHeader.substring(7);
				userName=jwtUtil.extractUserName(token);
				claims=jwtUtil.extractAllClaims(token);
				
				if(userName!=null && SecurityContextHolder.getContext().getAuthentication()==null) {
					
					UserDetails userDetail= service.loadUserByUsername(userName);
					if(jwtUtil.isTokenValid(token, userDetail)) {
						
						UsernamePasswordAuthenticationToken authToken= new UsernamePasswordAuthenticationToken(userDetail, null, userDetail.getAuthorities());
						authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
						SecurityContextHolder.getContext().setAuthentication(authToken);
					}
				}
			filterChain.doFilter(request, response);
				
				
			}
			
		}
		
	}
	
	public boolean isAdmin() {
		return "admin".equalsIgnoreCase((String) claims.get("role"));
	}
	
	public boolean isUser() {
		return "user".equalsIgnoreCase((String) claims.get("role"));
	}

	public String getCurrentUser() {
		return userName;
	}

}

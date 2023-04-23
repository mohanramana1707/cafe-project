package com.inn.cafe.JWT;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;


// conatins methods to work with JWT ( generate token, extract claims etc..)
@Service
@Slf4j
public class JwtUtil {
	
	
	
	private static final String secret="b3JkaW5hcnliZWF1dGlmdWxzZW5zZXVzZmFzdG1pZ2h0eXN0cnVnZ2xlZGVncmVlbG8=";   // to avoid tampering(signature)
	
	//getting the subject from token
	public String extractUserName(String token) {                             // extract a userName from token
		
		log.error("jwtUtil class: EXTRACT UserName ");
		
		return extractClaim(token, Claims::getSubject);  // get subject returns email id of the principle
	}
	
	
	// generic method to return a each specific claim from token
	public <T> T extractClaim(String token,Function<Claims, T> claimResolver){   // extract a single Claim(where claims is i/p type ,T is o/p type)
		
		final Claims claims=extractAllClaims(token);
		return claimResolver.apply(claims);  // apply the function to given statement
	}
	
	// to extract all claims from token
	public Claims extractAllClaims(String token) {							// extract all Claim
		
		log.error("jwtUtil class: EXTRACT allClaims ");
		return Jwts
				.parserBuilder()
				.setSigningKey(getSignInKey())   // verifying the user using signinkey(signature(secret))
				.build()
				.parseClaimsJws(token)
				.getBody();
		
		
		
	}
	
	//decoding the signature i.e secret key
	private Key getSignInKey() {
		
		byte[] keyBytes=Decoders.BASE64.decode(secret);   // for claim signature.
		return Keys.hmacShaKeyFor(keyBytes);
		//return Keys.secretKeyFor(SignatureAlgorithm.HS512);
		
	}
	
	// getting expiration from token
	private Date extractExpiration(String token) {
		
		return extractClaim(token, Claims::getExpiration);    //
	}
	
	// to check if the token is not expired
	private boolean isTokenExpired(String token) {
		
		return extractExpiration(token).before(new Date());
	}
	
	// to validate the token to approval ( username of token with the user name in DB)
	public boolean isTokenValid(String token, UserDetails userDetails) {
		final String userName= extractUserName(token);
		return ( userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}
	
	// generate a new token(changes made)
	
	public String generateToken(String userName ,String role) {      // generate token using user given detail while registering(signup)
		
		Map<String,Object> claims= new HashMap<>();  // just to store claims
		claims.put("role", role);
		return generateToken(claims, userName);
	}
	
	private String generateToken(Map<String,Object> claims, String userName) {
		
		return Jwts
				.builder()
				.setClaims(claims)      
				.setSubject(userName)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() +1000*60*100))
				.signWith(getSignInKey(), SignatureAlgorithm.HS256)
				.compact();
				
	}
	

}

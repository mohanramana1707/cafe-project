package com.inn.cafe.JWT;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;


// conatins methods to work with JWT ( generate token, extract claims etc..)
@Service
public class JwtUtil {
	
	private static final String secret="btechDays";   // to avoid tampering(signature)
	
	//getting the subject from token
	public String extractUserName(String token) {                             // extract a userName from token
		
		return extractClaim(token, Claims::getSubject);
	}
	
	
	// generic method to return a specific claim from token
	public <T> T extractClaim(String token,Function<Claims, T> claimResolver){   // extract a single Claim(where claims is i/p type ,T is o/p type)
		
		final Claims claims=extractAllClaims(token);
		return claimResolver.apply(claims);  // apply the function to given statement
	}
	
	// to extract all claims from token
	public Claims extractAllClaims(String token) {							// extract all Claim
		return Jwts
				.parserBuilder()
				.setSigningKey(getSignInKey())   // vertifying the user using signinkey(signature(secret))
				.build()
				.parseClaimsJws(token)
				.getBody();
	}
	
	//decoding the signature i.e secret key
	private Key getSignInKey() {
		
		byte[] keyBytes=Decoders.BASE64.decode(secret);   // for claim signature.
		return Keys.hmacShaKeyFor(keyBytes);
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
	
	public String generateToken(UserDetails userDetails ,String role) {      // generate token using user given detail while registering(signup)
		
		Map<String,Object> claims= new HashMap<>();
		claims.put("role", role);
		return generateToken(claims, userDetails);
	}
	
	private String generateToken(Map<String,Object> claims, UserDetails userDetails) {
		
		return Jwts
				.builder()
				.setClaims(claims)      
				.setSubject(userDetails.getUsername())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() +1000*60*24))
				.signWith(getSignInKey(), SignatureAlgorithm.HS256)
				.compact();
				
	}
	

}

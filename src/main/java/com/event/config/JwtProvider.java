package com.event.config;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtProvider {
	SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());
	
	public String generateToken(Authentication auth) {
		String jwt = Jwts.builder()
				.setIssuedAt(new Date())
				.setExpiration(new Date(new Date().getTime()+846000000))
				.claim("username", auth.getName())
				.signWith(key).compact();
		
		return jwt;
	}	
	
	public String getUsernameFromToken(String jwt) {
		jwt = jwt.substring(7);
		
		Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt).getBody();
		
		String username = String.valueOf(claims.get("username"));
		
		return username;
	}
	
	public void invalidateToken(String token) {
	    // Implement token invalidation logic if needed
	}

}

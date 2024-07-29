package com.yuvraj.config;

import java.io.IOException;
import java.util.List;

import javax.crypto.SecretKey;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtValidator extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String jwt = request.getHeader(JwtConstant.JWT_HEADER);
		
		if(jwt!=null) {
			// Bearere kjsgdabjbnxjkb
			jwt = jwt.substring(7); // We have token in above form and we will get it along Bearere keyword 
			                          // but we need to extract Bearere keyword fromit that why we r using this substring method.
			
			try {
				SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());
				
				Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt).getBody();
				
				String username = String.valueOf(claims.get("username"));
				
				String authorities = String.valueOf(claims.get("authorities"));
				
				List<GrantedAuthority> auths = AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);
				
				Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, auths);
				
				SecurityContextHolder.getContext().setAuthentication(authentication);
				
			} catch (Exception e) {
				throw new BadCredentialsException("invalid token... from jwt validator");
			}
		}
		
		filterChain.doFilter(request, response);
	}

}

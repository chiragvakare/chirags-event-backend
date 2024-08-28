package com.event.model;

public class AuthenticationResponse {
	
	private String jwt;
	private String message;
	
	public AuthenticationResponse() {
		// TODO Auto-generated constructor stub
	}
	
	public AuthenticationResponse(String jwt, String message) {
		super();
		this.jwt = jwt;
		this.message = message;
	}

	
	public String getJwt() {
		return jwt;
	}

	public void setJwt(String jwt) {
		this.jwt = jwt;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
}

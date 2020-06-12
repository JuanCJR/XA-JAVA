package com.databorough.utils;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class AuthenticationResponse {
	private Boolean authenticated;
	private String authToken;
	private String userDesc;

	public Boolean getAuthenticated() {
		return authenticated;
	}

	public void setAuthenticated(Boolean authenticated) {
		this.authenticated = authenticated;
	}
	
	public String getAuthToken() {
		return authToken;
	}

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}
	
	public String getUserDesc() {
		return userDesc;
	}

	public void setUserDesc(String userDesc) {
		this.userDesc = userDesc;
	}
}
package com.khs.sherpa.json.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import com.khs.sherpa.exception.SherpaPermissionExcpetion;

public class SpringAuthentication implements UserService {

//	private ApplicationContext ctx;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
//	public SpringAuthentication(ApplicationContext ctx) {
//		this.ctx = ctx;
//		authenticationManager = this.ctx.getBean(AuthenticationManager.class);
//	}
	
	public String[] authenticate(String username, String password) {
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
		
		Authentication authentication = authenticationManager.authenticate(token);
		if(authentication.isAuthenticated() == false) {
			throw new SherpaPermissionExcpetion("username and/or password is incorrect");
		}
		
		List<String> roles = new ArrayList<String>();
		for(GrantedAuthority auth: authentication.getAuthorities()) {
			roles.add(auth.getAuthority());
		}
		
		return roles.toArray(new String[roles.size()]);
		
	}


	public AuthenticationManager getAuthenticationManager() {
		return authenticationManager;
	}


	public void setAuthenticationManager(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

}

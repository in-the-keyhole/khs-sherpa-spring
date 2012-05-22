package com.khs.sherpa.json.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import com.khs.sherpa.exception.SherpaPermissionExcpetion;

public class SpringAuthentication implements UserService {

//	private ApplicationContext ctx;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
//	public SpringAuthentication(ApplicationContext ctx) {
//		this.ctx = ctx;
//		authenticationManager = this.ctx.getBean(AuthenticationManager.class);
//	}
	
	public void authenticate(String username, String password) {
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
		
		Authentication authentication = authenticationManager.authenticate(token);
		if(authentication.isAuthenticated() == false) {
			throw new SherpaPermissionExcpetion("username and/or password is incorrect");
		}
	}
	

	public void adminAuthenticate(String userid, String password) throws AuthenticationException {
		// TODO Auto-generated method stub
	}


	public AuthenticationManager getAuthenticationManager() {
		return authenticationManager;
	}


	public void setAuthenticationManager(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

}

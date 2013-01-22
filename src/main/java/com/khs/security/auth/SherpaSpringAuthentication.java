package com.khs.security.auth;

/*
 * Copyright 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;

import com.khs.sherpa.exception.SherpaPermissionExcpetion;
import com.khs.sherpa.json.service.UserService;
import com.khs.sherpa.spring.SpringAuthentication;

public class SherpaSpringAuthentication extends SpringAuthentication implements UserService {

	@Autowired
	private AuthenticationManager authenticationManager;
	
	
	@Autowired
	private RoleHandler customRoleVoter;	

	
	
	public String[] authenticate(String username, String password, HttpServletRequest request, HttpServletResponse response) {
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
		
		Authentication authentication = authenticationManager.authenticate(token);
		if(authentication.isAuthenticated() == false) {
			throw new SherpaPermissionExcpetion("username and/or password is incorrect");
		}
	
		SecurityContextImpl context = new SecurityContextImpl();
		context.setAuthentication(authentication);
		
		SecurityContextHolder.setContext(context);
		
		request.getSession().setAttribute("SPRING_SECURITY_CONTEXT_KEY", context);
		

		return customRoleVoter.authenticatedRoles(authentication);
	}

}

package com.khs.security.auth;

import java.util.List;

import org.springframework.security.core.Authentication;

public interface RoleHandler {

	public String[] authenticatedRoles(Authentication authentication);
	
}

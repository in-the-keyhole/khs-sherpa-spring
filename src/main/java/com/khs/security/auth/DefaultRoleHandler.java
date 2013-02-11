package com.khs.security.auth;

import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class DefaultRoleHandler extends RoleVoter implements RoleHandler {

	public String[] authenticatedRoles(Authentication authentication) {
		return new String[]{};
	}

}

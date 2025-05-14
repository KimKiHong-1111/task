package com.example.task.global.config;

import org.springframework.security.authentication.AbstractAuthenticationToken;

import com.example.task.auth.vo.AuthUser;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

	private final AuthUser authUser;

	public JwtAuthenticationToken(AuthUser authUser) {
		super(authUser.getRoles());
		this.authUser = authUser;
		setAuthenticated(true);
	}

	@Override
	public Object getCredentials() {
		return null;
	}

	@Override
	public Object getPrincipal() {
		return authUser;
	}
}

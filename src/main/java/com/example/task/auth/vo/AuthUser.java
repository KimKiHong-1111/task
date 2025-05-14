package com.example.task.auth.vo;

import java.util.List;

import lombok.Getter;

@Getter
public class AuthUser {

	private final Long id;
	private final String email;
	private final List<UserRole> authority;

	public AuthUser(Long id, String email, UserRole userRole) {
		this.id = id;
		this.email = email;
		this.authority = List.of(userRole);
	}
}

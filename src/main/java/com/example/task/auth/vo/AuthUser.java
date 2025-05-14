package com.example.task.auth.vo;

import java.util.List;

import lombok.Getter;

@Getter
public class AuthUser {

	private final Long id;
	private final String username;
	private final String nickname;
	private final List<UserRole> roles;

	public AuthUser(Long id, String username, String nickname, List<UserRole> roles) {
		this.id = id;
		this.username = username;
		this.nickname = nickname;
		this.roles = roles;
	}
}

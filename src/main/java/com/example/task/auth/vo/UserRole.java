package com.example.task.auth.vo;

import static com.example.task.global.exception.ErrorCode.*;

import java.util.Arrays;

import org.springframework.security.core.GrantedAuthority;

import com.example.task.global.exception.UnauthorizedException;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum UserRole implements GrantedAuthority {
	ROLE_USER(Authority.USER),
	ROLE_ADMIN(Authority.ADMIN);

	private final String userRole;

	public static UserRole of(String role) {
		return Arrays.stream(UserRole.values())
			.filter(r -> r.name().equalsIgnoreCase(role))
			.findFirst()
			.orElseThrow(() -> new UnauthorizedException(INVALID_USER_ROLE));
	}

	@Override
	public String getAuthority() {
		return name();
	}

	public static class Authority {
		public static final String USER = "USER";
		public static final String ADMIN = "ADMIN";
	}

}

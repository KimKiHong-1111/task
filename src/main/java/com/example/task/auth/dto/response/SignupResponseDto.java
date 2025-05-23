package com.example.task.auth.dto.response;

import java.util.Set;

import com.example.task.auth.entity.User;
import com.example.task.auth.vo.UserRole;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder
@Getter
@RequiredArgsConstructor
public class SignupResponseDto {
	private final String username;
	private final String nickname;
	private final Set<UserRole> roles;

	public static SignupResponseDto toDto(User user) {
		return SignupResponseDto.builder()
			.username(user.getUsername())
			.nickname(user.getNickname())
			.roles(user.getRoles())
			.build();
	}

}

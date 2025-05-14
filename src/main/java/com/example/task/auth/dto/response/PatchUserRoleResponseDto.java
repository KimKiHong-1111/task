package com.example.task.auth.dto.response;

import java.util.List;

import com.example.task.auth.entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class PatchUserRoleResponseDto {
	private final String username;
	private final String nickname;
	private final List<RoleDto> roles;

	public static PatchUserRoleResponseDto toDto(User user) {
		return PatchUserRoleResponseDto.builder()
			.username(user.getUsername())
			.nickname(user.getNickname())
			.roles(user.getRoles().stream()
				.map(role -> new RoleDto(role.name()))
				.toList())
			.build();
	}

	@Getter
	@AllArgsConstructor
	public static class RoleDto {
		private String role;
	}
}

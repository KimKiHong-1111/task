package com.example.task.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequestDto {
	//username,password,nickname
	@NotBlank
	private String username;
	@NotBlank
	private String password;
	@NotBlank
	private String nickname;
}

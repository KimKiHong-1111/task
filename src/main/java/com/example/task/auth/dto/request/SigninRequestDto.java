package com.example.task.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SigninRequestDto {
	//username,password
	@NotBlank
	private String username;
	@NotBlank
	private String password;
}

package com.example.task.auth.controller;

import static com.example.task.auth.vo.UserRole.Authority.*;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.task.auth.dto.request.SigninRequestDto;
import com.example.task.auth.dto.request.SignupRequestDto;
import com.example.task.auth.dto.response.PatchUserRoleResponseDto;
import com.example.task.auth.dto.response.SigninResponseDto;
import com.example.task.auth.dto.response.SignupResponseDto;
import com.example.task.auth.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class AuthController {

	private final AuthService authService;

	@PostMapping("/signup")
	public ResponseEntity<SignupResponseDto> signUp(
		@RequestBody SignupRequestDto signupRequestDto
	) {
		SignupResponseDto signupResponseDto = authService.signUp(signupRequestDto);

		return new ResponseEntity<>(signupResponseDto,HttpStatus.CREATED);
	}

	@PostMapping("/signin")
	public ResponseEntity<SigninResponseDto> signIn(
		@RequestBody SigninRequestDto signinRequestDto
	) {
		SigninResponseDto signinResponseDto = authService.signIn(signinRequestDto);

		String newToken = signinResponseDto.getToken();

		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", newToken);

		return new ResponseEntity<>(signinResponseDto, headers, HttpStatus.CREATED);
	}

	@PatchMapping("admin/users/{userId}/roles")
	@Secured("ADMIN")
	public ResponseEntity<PatchUserRoleResponseDto> grantAdminRole(
		@PathVariable Long userId
	) {
		PatchUserRoleResponseDto patchUserRoleResponseDto = authService.grantUserRole(userId);
		return new ResponseEntity<>(patchUserRoleResponseDto, HttpStatus.OK);
	}
}

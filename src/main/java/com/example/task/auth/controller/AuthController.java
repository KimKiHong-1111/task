package com.example.task.auth.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "인증/회원", description = "회원가입, 로그인, 권한 관리 API")
@RestController
@RequiredArgsConstructor
@RequestMapping
public class AuthController {

	private final AuthService authService;


	@Operation(
		summary = "회원가입",
		description = "새로운 사용자를 회원가입시킵니다."
	)
	@ApiResponses({
		@ApiResponse(responseCode = "201", description = "회원가입 성공", content = @Content(schema = @Schema(implementation = SignupResponseDto.class))),
		@ApiResponse(responseCode = "400", description = "잘못된 입력값"),
		@ApiResponse(responseCode = "409", description = "이미 존재하는 사용자")
	})
	@PostMapping("/signup")
	public ResponseEntity<SignupResponseDto> signUp(
		@io.swagger.v3.oas.annotations.parameters.RequestBody(
			description = "회원가입 요청 DTO",
			required = true,
			content = @Content(schema = @Schema(implementation = SignupRequestDto.class))
		)
		@RequestBody SignupRequestDto signupRequestDto
	) {
		SignupResponseDto signupResponseDto = authService.signUp(signupRequestDto);

		return new ResponseEntity<>(signupResponseDto,HttpStatus.CREATED);
	}

	@Operation(
		summary = "로그인",
		description = "이메일과 비밀번호로 로그인합니다. 응답 헤더에 JWT 토큰이 포함됩니다."
	)
	@ApiResponses({
		@ApiResponse(responseCode = "201", description = "로그인 성공", content = @Content(schema = @Schema(implementation = SigninResponseDto.class))),
		@ApiResponse(responseCode = "401", description = "인증 실패")
	})
	@PostMapping("/signin")
	public ResponseEntity<SigninResponseDto> signIn(
		@io.swagger.v3.oas.annotations.parameters.RequestBody(
			description = "로그인 요청 DTO",
			required = true,
			content = @Content(schema = @Schema(implementation = SigninRequestDto.class))
		)
		@RequestBody SigninRequestDto signinRequestDto
	) {
		SigninResponseDto signinResponseDto = authService.signIn(signinRequestDto);

		String newToken = signinResponseDto.getToken();

		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", newToken);

		return new ResponseEntity<>(signinResponseDto, headers, HttpStatus.CREATED);
	}

	@Operation(
		summary = "사용자 권한(관리자) 부여",
		description = "특정 사용자에게 관리자 권한을 부여합니다. (ADMIN 권한 필요)"
	)
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "권한 부여 성공", content = @Content(schema = @Schema(implementation = PatchUserRoleResponseDto.class))),
		@ApiResponse(responseCode = "403", description = "권한 없음"),
		@ApiResponse(responseCode = "404", description = "사용자 없음")
	})
	@PatchMapping("admin/users/{userId}/roles")
	@Secured("ADMIN")
	public ResponseEntity<PatchUserRoleResponseDto> grantAdminRole(
		@Parameter(description = "권한을 부여할 사용자 ID", required = true)
		@PathVariable Long userId
	) {
		PatchUserRoleResponseDto patchUserRoleResponseDto = authService.grantUserRole(userId);
		return new ResponseEntity<>(patchUserRoleResponseDto, HttpStatus.OK);
	}
}

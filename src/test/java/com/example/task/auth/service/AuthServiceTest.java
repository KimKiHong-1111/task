package com.example.task.auth.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.example.task.auth.dto.request.SigninRequestDto;
import com.example.task.auth.dto.request.SignupRequestDto;
import com.example.task.auth.dto.response.PatchUserRoleResponseDto;
import com.example.task.auth.dto.response.SigninResponseDto;
import com.example.task.auth.dto.response.SignupResponseDto;
import com.example.task.auth.entity.User;
import com.example.task.auth.repository.UserRepository;
import com.example.task.auth.vo.UserRole;
import com.example.task.global.exception.ConflictException;
import com.example.task.global.util.JwtUtil;

@ExtendWith(SpringExtension.class)
class AuthServiceTest {

	@Mock
	private UserRepository userRepository;
	@Mock
	private PasswordEncoder passwordEncoder;
	@Mock
	private JwtUtil jwtUtil;

	@InjectMocks
	private AuthService authService;

	//회원가입 성공 테스트
	@Test
	void signUp_Success() {
		//Given
		SignupRequestDto request = new SignupRequestDto("user1", "password", "nick1");
		when(userRepository.existsUserByNickname("nick1")).thenReturn(false);
		when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
		when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

		//When
		SignupResponseDto response = authService.signUp(request);

		//Then
		assertEquals("user1", response.getUsername());
		assertEquals("nick1", response.getNickname());
		assertTrue(response.getRoles().contains(UserRole.ROLE_USER));
	}

	// 중복 닉네임 테스트
	@Test
	void signUp_DuplicateNickname() {
		// Given
		SignupRequestDto request = new SignupRequestDto("user1", "password", "nick1");
		when(userRepository.existsUserByNickname("nick1")).thenReturn(true);

		// When & Then
		assertThrows(ConflictException.class, () -> authService.signUp(request));
	}

	// 로그인 성공 테스트
	@Test
	void signIn_Success() {
		// Given
		User user = User.builder()
			.username("user1")
			.password("encodedPassword")
			.roles(Set.of(UserRole.ROLE_USER))
			.build();
		when(userRepository.findByUsername("user1")).thenReturn(Optional.of(user));
		when(passwordEncoder.matches("password", "encodedPassword")).thenReturn(true);
		when(jwtUtil.createAccessToken(any(), any(), any(), any())).thenReturn("token");

		// When
		SigninResponseDto response = authService.signIn(new SigninRequestDto("user1", "password"));

		// Then
		assertEquals("token", response.getToken());
	}

	// 관리자 권한 부여 테스트
	@Test
	void grantAdminRole_Success() {
		// Given
		Set<UserRole> roles = new HashSet<>();
		roles.add(UserRole.ROLE_USER);
		User user = User.builder()
			.username("user1")
			.password("encodedPassword")
			.nickname("nick1")
			.roles(roles).build();

		when(userRepository.findById(1L)).thenReturn(Optional.of(user));
		when(userRepository.save(user)).thenReturn(user);

		// When
		PatchUserRoleResponseDto response = authService.grantUserRole(1L);

		// Then
		assertTrue(response.getRoles().stream().anyMatch(r -> r.getRole().equals("ROLE_ADMIN")));
	}
}

package com.example.task.auth.service;

import static com.example.task.global.exception.ErrorCode.*;

import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.task.auth.dto.request.SigninRequestDto;
import com.example.task.auth.dto.request.SignupRequestDto;
import com.example.task.auth.dto.response.PatchUserRoleResponseDto;
import com.example.task.auth.dto.response.SigninResponseDto;
import com.example.task.auth.dto.response.SignupResponseDto;
import com.example.task.auth.entity.User;
import com.example.task.auth.repository.UserRepository;
import com.example.task.auth.vo.UserRole;
import com.example.task.global.exception.BadRequestException;
import com.example.task.global.exception.ConflictException;
import com.example.task.global.exception.NotFoundException;
import com.example.task.global.util.JwtUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtil;

	@Transactional
	public SignupResponseDto signUp(SignupRequestDto signupRequestDto) {
		if (userRepository.existsUserByNickname(signupRequestDto.getNickname())) {
			throw new ConflictException(USER_ALREADY_EXISTS);
		}

		String encodedPassword = passwordEncoder.encode(signupRequestDto.getPassword());


		User newUser = User.builder()
			.nickname(signupRequestDto.getNickname())
			.password(encodedPassword)
			.username(signupRequestDto.getUsername())
			.roles(Set.of(UserRole.ROLE_USER))
			.build();

		User savedUser = userRepository.save(newUser);

		return SignupResponseDto.toDto(savedUser);
	}

	@Transactional
	public SigninResponseDto signIn(SigninRequestDto signinRequestDto) {
		User user = userRepository.findByUsername(signinRequestDto.getUsername())
			.orElseThrow(()-> new NotFoundException(USER_NOT_FOUND));

		if (!passwordEncoder.matches(signinRequestDto.getPassword(), user.getPassword())) {
			throw new BadRequestException(INVALID_CREDENTIALS);
		}

		String accessToken = jwtUtil.createAccessToken(
			user.getId(),
			user.getUsername(),
			user.getRoles(),
			user.getNickname()
		);

		return new SigninResponseDto(accessToken);
	}

	@Transactional
	public PatchUserRoleResponseDto grantUserRole(Long userId) {
		User user = userRepository.findById(userId)
			.orElseThrow(()-> new NotFoundException(USER_NOT_FOUND));

		// 역할 추가 로직
		if (!user.getRoles().contains(UserRole.ROLE_ADMIN)) {
			user.getRoles().add(UserRole.ROLE_ADMIN);
			User savedUser = userRepository.save(user);
			return PatchUserRoleResponseDto.toDto(savedUser);
		}
		return PatchUserRoleResponseDto.toDto(user);
	}

}

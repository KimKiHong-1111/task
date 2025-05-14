package com.example.task.auth.service;

import static com.example.task.global.exception.ErrorCode.*;

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
import com.example.task.auth.vo.AuthUser;
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
		UserRole userRole = UserRole.ROLE_USER;

		User newUser = User.builder()
			.nickname(signupRequestDto.getNickname())
			.password(encodedPassword)
			.userRole(userRole)
			.username(signupRequestDto.getUsername())
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
			user.getUserRole(),
			user.getNickname()
		);

		return new SigninResponseDto(accessToken);
	}

	@Transactional
	public PatchUserRoleResponseDto grantUserRole(AuthUser authUser) {
		User user = userRepository.findById(authUser.getId())
			.orElseThrow(()-> new NotFoundException(USER_NOT_FOUND));

		if(!user.getUserRole().contains(UserRole.ROLE_ADMIN)) {
			user.getUserRole().add(UserRole.ROLE_ADMIN);
			return userRepository.save(user);
		}
	}

}

package com.example.task.global.exception;

import static org.springframework.http.HttpStatus.*;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
	USER_NOT_FOUND("존재하지 않는 유저입니다.", NOT_FOUND),
	INVALID_USER_ROLE("유효하지 않은 역할입니다.", UNAUTHORIZED),
	NEED_LOGIN("재로그인이 필요합니다", UNAUTHORIZED),
	INVALID_ACCESS_TOKEN("유효하지 않은 토큰입니다.", UNAUTHORIZED),
	INVALID_TOKEN("유효하지 않은 인증 토큰입니다.", UNAUTHORIZED),
	USER_ALREADY_EXISTS("이미 가입된 사용자입니다.", CONFLICT),
	ACCESS_DENIED("관리자 권한이 필요한 요청입니다. 접근 권한이 없습니다.", UNAUTHORIZED),
	INTERNAL_SERVER_ERROR("내부 서버 오류입니다.", HttpStatus.INTERNAL_SERVER_ERROR),
	INVALID_CREDENTIALS("아이디 또는 비밀번호가 올바르지 않습니다.", NOT_FOUND);


	private final String message;
	private final HttpStatus status;
}

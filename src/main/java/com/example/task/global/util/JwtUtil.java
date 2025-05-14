package com.example.task.global.util;

import static com.example.task.global.exception.ErrorCode.*;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.example.task.auth.vo.UserRole;
import com.example.task.global.exception.UnauthorizedException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil {

	private static final String BEARER_PREFIX = "Bearer ";
	private static final long ACCESS_TOKEN_TIME = 60 * 60 * 2 * 1000L; //2시간

	@Value("${jwt.secret.key}")
	private String secretKey;
	private Key key;
	private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

	@PostConstruct
	public void init() {
		byte[] bytes = Base64.getDecoder().decode(secretKey);
		key = Keys.hmacShaKeyFor(bytes);
	}

	public String createAccessToken(Long userId, String username, Set<UserRole> roles, String nickname) {
		Date date = new Date();

		return Jwts.builder()
			.setSubject(String.valueOf(userId))
			.claim("username", username)
			.claim("roles", roles.stream().map(UserRole::getAuthority).collect(Collectors.toList()))
			.claim("nickname", nickname)
			.setExpiration(new Date(date.getTime() + ACCESS_TOKEN_TIME))
			.setIssuedAt(date) //발급일
			.signWith(key, signatureAlgorithm)
			.compact();
	}

	public String substringToken(String token) {
		if (StringUtils.hasText(token) && token.startsWith(BEARER_PREFIX)) {
			return token.substring(7);
		}
		throw new UnauthorizedException(INVALID_TOKEN);
	}

	public Claims extractClaims(String token) {
		return Jwts.parserBuilder()
			.setSigningKey(key)
			.build()
			.parseClaimsJws(token)
			.getBody();
	}
}

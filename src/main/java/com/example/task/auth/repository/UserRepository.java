package com.example.task.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.task.auth.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	boolean existsUserByNickname(String nickname);

	Optional<User> findByUsername(String username);
}

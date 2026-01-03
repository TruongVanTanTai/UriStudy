package com.tantai.uristudy.repository;

import com.tantai.uristudy.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByIdAndTokenAndExpirationTimeGreaterThanEqualAndIsVerifiedFalse(Long id, String token, LocalDateTime now);
    Token findTopByEmailOrderByIdDesc(String email);
}

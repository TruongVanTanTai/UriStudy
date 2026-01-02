package com.tantai.uristudy.repository;

import com.tantai.uristudy.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, Long> {
}

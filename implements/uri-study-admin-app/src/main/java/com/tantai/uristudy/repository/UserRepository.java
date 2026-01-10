package com.tantai.uristudy.repository;

import com.tantai.uristudy.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}

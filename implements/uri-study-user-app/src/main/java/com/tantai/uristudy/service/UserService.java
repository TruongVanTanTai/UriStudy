package com.tantai.uristudy.service;

import com.tantai.uristudy.dto.request.UserCreationRequest;
import com.tantai.uristudy.entity.User;
import com.tantai.uristudy.exception.PasswordMismatchException;
import com.tantai.uristudy.exception.UsernameAlreadyExistsException;
import com.tantai.uristudy.mapper.UserMapper;
import com.tantai.uristudy.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;

    public User createUser(UserCreationRequest userCreationRequest) {
        if (!userCreationRequest.getPassword().equals(userCreationRequest.getConfirmPassword())) {
            throw new PasswordMismatchException("Mật khẩu xác nhận không khớp");
        }

        if (userRepository.findByUsername(userCreationRequest.getUsername()).orElse(null) != null) {
            throw new UsernameAlreadyExistsException("Tên đăng nhập đã tồn tại");
        }

        userCreationRequest.setPassword(passwordEncoder.encode(userCreationRequest.getPassword()));
        return userRepository.save(userMapper.toUser(userCreationRequest));
    }
}

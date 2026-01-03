package com.tantai.uristudy.mapper;

import com.tantai.uristudy.dto.request.UserCreationRequest;
import com.tantai.uristudy.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public User toUser(UserCreationRequest userCreationRequest) {
        User user = User.builder()
                .name(userCreationRequest.getName())
                .email(userCreationRequest.getEmail())
                .phoneNumber(userCreationRequest.getPhoneNumber())
                .dateOfBirth(userCreationRequest.getDateOfBirth())
                .isMale(userCreationRequest.getIsMale())
                .address(userCreationRequest.getAddress())
                .username(userCreationRequest.getUsername())
                .password(userCreationRequest.getPassword())
                .build();

        return user;
    }
}

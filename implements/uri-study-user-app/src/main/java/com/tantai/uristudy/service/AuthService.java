package com.tantai.uristudy.service;

import com.tantai.uristudy.entity.Token;
import com.tantai.uristudy.exception.EmailAlreadyExistsException;
import com.tantai.uristudy.infrastructure.EmailSender;
import com.tantai.uristudy.infrastructure.TokenGenerator;
import com.tantai.uristudy.repository.TokenRepository;
import com.tantai.uristudy.repository.UserRepository;
import jakarta.mail.MessagingException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AuthService {
    TokenGenerator tokenGenerator;
    EmailSender emailSender;
    TokenRepository tokenRepository;
    UserRepository userRepository;

    public void sendEmailVerification(String email) {
        if (userRepository.findByEmail(email) != null) {
            throw new EmailAlreadyExistsException("Email đã tồn tại");
        }

        Token token = Token.builder()
                .token(tokenGenerator.generateToken())
                .email(email)
                .build();

        Token savedToken = tokenRepository.save(token);
        try {
            Map<String, Object> data = new HashMap<>();
            data.put("id", savedToken.getId());
            data.put("token", savedToken.getToken());
            emailSender.sendEmail(email, "Yêu cầu xác thực email", "email", data);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public void verifyEmail(String id, String token) {

    }
}

package com.tantai.uristudy.service;

import com.tantai.uristudy.dto.request.UserCreationRequest;
import com.tantai.uristudy.dto.request.UserEditRequest;
import com.tantai.uristudy.entity.FlashCardSet;
import com.tantai.uristudy.entity.User;
import com.tantai.uristudy.exception.PasswordMismatchException;
import com.tantai.uristudy.exception.UserNotFoundException;
import com.tantai.uristudy.exception.UsernameAlreadyExistsException;
import com.tantai.uristudy.mapper.UserMapper;
import com.tantai.uristudy.repository.FlashCardSetRepository;
import com.tantai.uristudy.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    private final FlashCardSetRepository flashCardSetRepository;

    @Transactional
    public User createUser(UserCreationRequest userCreationRequest) {
        if (!userCreationRequest.getPassword().equals(userCreationRequest.getConfirmPassword())) {
            throw new PasswordMismatchException("Mật khẩu xác nhận không khớp");
        }

        if (userRepository.findByUsername(userCreationRequest.getUsername()).orElse(null) != null) {
            throw new UsernameAlreadyExistsException("Tên đăng nhập đã tồn tại");
        }

        userCreationRequest.setPassword(passwordEncoder.encode(userCreationRequest.getPassword()));
        User user = userRepository.save(userMapper.toUser(userCreationRequest));

        FlashCardSet FavoriteVocabularyfFashCardSet = FlashCardSet.builder()
                .name("Bộ flash card từ vựng yêu thích")
                .type(false)
                .modifiedDate(LocalDateTime.now())
                .description("Bộ flash cart chứa tất cả những flash cart từ vựng yêu thích của bạn")
                .isFavorite(true)
                .isPublic(false)
                .user(user)
                .build();
        flashCardSetRepository.save(FavoriteVocabularyfFashCardSet);

        FlashCardSet FavoriteGrammarfFashCardSet = FlashCardSet.builder()
                .name("Bộ flash card ngữ pháp yêu thích")
                .type(true)
                .modifiedDate(LocalDateTime.now())
                .description("Bộ flash cart chứa tất cả những flash cart ngữ pháp yêu thích của bạn")
                .isFavorite(true)
                .isPublic(false)
                .user(user)
                .build();
        flashCardSetRepository.save(FavoriteGrammarfFashCardSet);

        return user;
    }

    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN') and authentication.principal.user.id == #id")
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Không tồn tại người dùng có id: " + id));
    }

    @PreAuthorize("hasAuthority('USER') and authentication.principal.user.id == #id")
    public User updateUser(Long id, UserEditRequest userEditRequest) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Không tồn tại người dùng có id: " + id));

        user.setName(userEditRequest.getName());
        user.setPhoneNumber(userEditRequest.getPhoneNumber());
        user.setDateOfBirth(userEditRequest.getDateOfBirth());
        user.setIsMale(userEditRequest.getIsMale());
        user.setAddress(userEditRequest.getAddress());

        return userRepository.save(user);
    }
}

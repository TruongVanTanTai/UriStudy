package com.tantai.uristudy.service;

import com.tantai.uristudy.dto.request.FlashCardSetCreationRequest;
import com.tantai.uristudy.entity.FlashCardSet;
import com.tantai.uristudy.entity.User;
import com.tantai.uristudy.exception.ImageCreationException;
import com.tantai.uristudy.exception.UserNotFoundException;
import com.tantai.uristudy.infrastructure.ImageCreator;
import com.tantai.uristudy.mapper.FlashCardSetMapper;
import com.tantai.uristudy.repository.FlashCardSetRepository;
import com.tantai.uristudy.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class FlashCardSetService {
    FlashCardSetRepository flashCardSetRepository;
    FlashCardSetMapper flashCardSetMapper;
    UserRepository userRepository;
    ImageCreator imageCreator;

    public Page<FlashCardSet> getFlashCardSetsByUserId(Long userId, int page, int size) {
        if (page <= 0) page = 1;
        if (size <= 0)  size = 6;

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "modifiedDate"));
        return flashCardSetRepository.findByUserId(userId, pageable);
    }

    public Page<FlashCardSet> getFlashCardSetsByUserIdAndType(Long userId, boolean type, int page, int size) {
        if (page <= 0) page = 1;
        if (size <= 0) size = 6;

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "modifiedDate"));
        return flashCardSetRepository.findByUserIdAndType(userId, type, pageable);
    }

    public Page<FlashCardSet> getFlashCardSetsByIdAndByName(Long userId, String name, int page, int size) {
        if (page <= 0) page = 1;
        if (size <= 0) size = 6;

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "modifiedDate"));
        return flashCardSetRepository.findByUserIdAndNameContaining(userId, name, pageable);
    }

    public Page<FlashCardSet> getFavoriteFlashCardSets(Long userId, int page, int size) {
        if (page <= 0) page = 1;
        if (size <= 0) size = 6;

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "modifiedDate"));
        return flashCardSetRepository.findByUserIdAndIsFavoriteTrue(userId, pageable);
    }

    public FlashCardSet createFlashCardSet(Long userId, FlashCardSetCreationRequest flashCardSetCreationRequest) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("Không tìm thấy người dùng có id:" + userId));

        String imagePath = null;
        if (!flashCardSetCreationRequest.getImage().isEmpty()) {
            try {
                imagePath = imageCreator.createImage(flashCardSetCreationRequest.getImage(), "flash-card-set-images");
            }
            catch (IOException e) {
                throw new ImageCreationException("Tạo ảnh thất bại");
            }
        }

        FlashCardSet flashCardSet = flashCardSetMapper.toFlashCardSet(flashCardSetCreationRequest);
        flashCardSet.setModifiedDate(LocalDateTime.now());
        flashCardSet.setIsFavorite(false);
        flashCardSet.setImage(imagePath);
        flashCardSet.setUser(user);

        return flashCardSetRepository.save(flashCardSet);
    }
}

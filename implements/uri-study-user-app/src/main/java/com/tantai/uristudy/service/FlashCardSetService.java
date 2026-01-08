package com.tantai.uristudy.service;

import com.tantai.uristudy.dto.request.FlashCardSetCreationRequest;
import com.tantai.uristudy.dto.request.FlashCardSetEditRequest;
import com.tantai.uristudy.entity.FlashCardSet;
import com.tantai.uristudy.entity.User;
import com.tantai.uristudy.exception.FlashCardSetNotFoundException;
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
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @PreAuthorize("hasAuthority('USER') and authentication.principal.user.id == #userId")
    public Page<FlashCardSet> getFlashCardSetsByUserId(Long userId, int page, int size) {
        if (page <= 0) page = 1;
        if (size <= 0)  size = 6;

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "modifiedDate"));
        return flashCardSetRepository.findByUserId(userId, pageable);
    }

    @PreAuthorize("hasAuthority('USER') and authentication.principal.user.id == #userId")
    public Page<FlashCardSet> getFlashCardSetsByUserIdAndType(Long userId, boolean type, int page, int size) {
        if (page <= 0) page = 1;
        if (size <= 0) size = 6;

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "modifiedDate"));
        return flashCardSetRepository.findByUserIdAndType(userId, type, pageable);
    }

    @PreAuthorize("hasAuthority('USER') and authentication.principal.user.id == #userId")
    public Page<FlashCardSet> getFlashCardSetsByIdAndByName(Long userId, String name, int page, int size) {
        if (page <= 0) page = 1;
        if (size <= 0) size = 6;

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "modifiedDate"));
        return flashCardSetRepository.findByUserIdAndNameContaining(userId, name, pageable);
    }

    @PreAuthorize("hasAuthority('USER') and authentication.principal.user.id == #userId")
    public Page<FlashCardSet> getFavoriteFlashCardSets(Long userId, int page, int size) {
        if (page <= 0) page = 1;
        if (size <= 0) size = 6;

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "modifiedDate"));
        return flashCardSetRepository.findByUserIdAndIsFavoriteTrue(userId, pageable);
    }

    @PreAuthorize("hasAuthority('USER') and authentication.principal.user.id == #userId")
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

    @PostAuthorize("hasAuthority('USER') and authentication.principal.user.id == returnObject.user.id")
    public FlashCardSet getFlashCardSetById(Long id) {
        return flashCardSetRepository.findById(id)
                .orElseThrow(() -> new FlashCardSetNotFoundException("Không tìm thấy bộ flash card có id: " + id));
    }

    @PreAuthorize("hasAuthority('USER') and authentication.principal.user.id == #flashCardSetEditRequest.user.id")
    public FlashCardSet editFashCartSet(FlashCardSetEditRequest flashCardSetEditRequest) {
        FlashCardSet flashCardSet = flashCardSetRepository.findByIdAndUserId(flashCardSetEditRequest.getId(), flashCardSetEditRequest.getUser().getId())
                .orElseThrow(() -> new FlashCardSetNotFoundException("Không tìm thấy bộ flash card có id: " + flashCardSetEditRequest.getId() + "của người dùng có id: " + flashCardSetEditRequest.getUser().getId()));

        flashCardSet.setName(flashCardSetEditRequest.getName());
        flashCardSet.setType(flashCardSetEditRequest.getType());
        flashCardSet.setModifiedDate(LocalDateTime.now());
        flashCardSet.setDescription(flashCardSetEditRequest.getDescription());
        flashCardSet.setIsPublic(flashCardSetEditRequest.getIsPublic());

        if (!flashCardSetEditRequest.getImage().isEmpty()) {
            try {
                String imagePath = imageCreator.createImage(flashCardSetEditRequest.getImage(), "flash-card-set-images");
                flashCardSet.setImage(imagePath);
            }
            catch (IOException e) {
                throw new ImageCreationException("Tạo ảnh thất bại");
            }
        }

        return flashCardSetRepository.save(flashCardSet);
    }

    @PreAuthorize("hasAuthority('USER') and authentication.principal.user.id == #flashCardSet.user.id")
    @Transactional
    public void deleteFlashCardSet(FlashCardSet flashCardSet) {
        flashCardSetRepository.deleteByIdAndUserId(flashCardSet.getId(), flashCardSet.getUser().getId());
    }
}

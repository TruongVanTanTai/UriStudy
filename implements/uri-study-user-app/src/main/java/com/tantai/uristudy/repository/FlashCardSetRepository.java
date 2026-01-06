package com.tantai.uristudy.repository;

import com.tantai.uristudy.entity.FlashCardSet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlashCardSetRepository extends JpaRepository<FlashCardSet, Long> {
    Page<FlashCardSet> findByUserId(Long userId, Pageable pageable);
    Page<FlashCardSet> findByUserIdAndType(Long userId, boolean type, Pageable pageable);
    Page<FlashCardSet> findByUserIdAndNameContaining(Long userId, String name, Pageable pageable);
    Page<FlashCardSet> findByUserIdAndIsFavoriteTrue(Long userId, Pageable pageable);
}

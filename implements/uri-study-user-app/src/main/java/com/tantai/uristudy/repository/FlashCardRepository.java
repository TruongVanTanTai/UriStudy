package com.tantai.uristudy.repository;

import com.tantai.uristudy.entity.FlashCard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FlashCardRepository extends JpaRepository<FlashCard,Long> {
    Page<FlashCard> findByFlashCardSetId(Long userId, Pageable pageable);

    @Query("""
    SELECT  f
    FROM    FlashCard f
    WHERE   f.flashCardSet.id = :flashCardSetId AND (f.term LIKE %:keyword% OR f.definition LIKE %:keyword%)
        """)
    Page<FlashCard> searchFlashCard(@Param("flashCardSetId") Long flashCardSetId, @Param("keyword") String keyword, Pageable pageable);

    Optional<FlashCard> findByIdAndFlashCardSetId(Long id, Long flashCardSetId);
    void deleteByIdAndFlashCardSetId(Long id, Long flashCardSetId);
}

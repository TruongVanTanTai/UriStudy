package com.tantai.uristudy.repository;

import com.tantai.uristudy.entity.FlashCardSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FlashCardSetRepository extends JpaRepository<FlashCardSet, Long> {
    @Query(value = """
        SELECT
            CASE WHEN IsPublic = 1 THEN N'Công khai' ELSE N'Riêng tư' END as status,
            COUNT(Id) as quantity
        FROM FlashCardSet
        GROUP BY IsPublic
    """, nativeQuery = true)
    List<Object[]> statisticByShareStatus();

    @Query(value = """
        SELECT
            CASE WHEN Type = 1 THEN N'Ngữ pháp' ELSE N'Từ vựng' END as status,
            COUNT(Id) as quantity
        FROM FlashCardSet
        GROUP BY Type
    """, nativeQuery = true)
    List<Object[]> statisticType();
}

package com.tantai.uristudy.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "FlashCardSetReview")
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class FlashCardSetReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", columnDefinition = "BIGINT", nullable = false)
    Long id;

    @Column(name = "Rating", columnDefinition = "TINYINT", nullable = false)
    Integer rating;

    @Column(name = "Comment", columnDefinition = "NVARCHAR(3000)", length = 3000, nullable = false)
    String comment;

    @ManyToOne
    @JoinColumn(name = "UserId", nullable = false)
    User user;

    @ManyToOne
    @JoinColumn(name = "FlashCardSetId", nullable = false)
    FlashCardSet flashCardSet;
}

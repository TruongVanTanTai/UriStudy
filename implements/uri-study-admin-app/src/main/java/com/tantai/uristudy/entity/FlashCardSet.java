package com.tantai.uristudy.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "FlashCardSet")
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class FlashCardSet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", columnDefinition = "BIGINT", nullable = false)
    Long id;

    @Column(name = "Name", columnDefinition = "NVARCHAR(100)", length = 100, nullable = false)
    String name;

    @Column(name = "Type", columnDefinition = "BIT", nullable = false)
    Boolean type;

    @Column(name = "ModifiedDate", columnDefinition = "DATETIME", nullable = false)
    LocalDateTime modifiedDate;

    @Column(name = "Description", columnDefinition = "NVARCHAR(3000)", length = 3000)
    String description;

    @Column(name = "IsFavorite", columnDefinition = "BIT", nullable = false)
    Boolean isFavorite;

    @Column(name = "IsPublic", columnDefinition = "BIT", nullable = false)
    Boolean isPublic;

    @Column(name = "Image", columnDefinition = "NVARCHAR(1000)")
    String image;

    @ManyToOne
    @JoinColumn(name = "UserId", nullable = false)
    User user;

    @OneToMany(mappedBy = "flashCardSet", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<FlashCard> flashCards;

    @OneToMany(mappedBy = "flashCardSet", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<FlashCardSetReview> flashCardSetReviews;
}
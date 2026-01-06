package com.tantai.uristudy.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "FlashCard")
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class FlashCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", columnDefinition = "BIGINT", nullable = false)
    Long id;

    @Column(name = "Term", columnDefinition = "NVARCHAR(3000)", length = 3000, nullable = false)
    String term;

    @Column(name = "Definition", columnDefinition = "NVARCHAR(3000)", length = 3000, nullable = false)
    String definition;

    @Column(name = "Note", columnDefinition = "NVARCHAR(3000)", length = 3000)
    String note;

    @Column(name = "Example", columnDefinition = "NVARCHAR(3000)", length = 3000)
    String example;

    @ManyToOne
    @JoinColumn(name = "FlashCardSetId", nullable = false)
    FlashCardSet flashCardSet;
}

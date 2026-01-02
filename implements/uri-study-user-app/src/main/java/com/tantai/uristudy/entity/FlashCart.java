package com.tantai.uristudy.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "FlashCart")
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class FlashCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", columnDefinition = "BIGINT", nullable = false)
    Long id;

    @Column(name = "Term", columnDefinition = "NVARCHAR(3000)", length = 3000, nullable = false)
    String term;

    @Column(name = "Definition", columnDefinition = "NVARCHAR(3000)", length = 3000, nullable = false)
    String definition;

    @Column(name = "Note", columnDefinition = "NVARCHAR(3000)", length = 3000, nullable = false)
    String note;

    @Column(name = "Example", columnDefinition = "NVARCHAR(3000)", length = 3000, nullable = false)
    String example;

    @ManyToOne
    @JoinColumn(name = "FlashCartSetId", nullable = false)
    FlashCartSet flashCartSet;
}

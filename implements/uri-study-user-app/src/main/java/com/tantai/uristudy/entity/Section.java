package com.tantai.uristudy.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@Table(name = "Section")
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class Section {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", columnDefinition = "BIGINT", nullable = false)
    Long id;

    @Column(name = "Type", columnDefinition = "INT", nullable = false)
    Integer type;

    @Column(name = "Audio", columnDefinition = "NVARCHAR(1000)")
    String audio;

    @Column(name = "Name", columnDefinition = "NVARCHAR(3000)", nullable = false)
    String name;

    @ManyToOne
    @JoinColumn(name = "ExamId", nullable = false)
    Exam exam;

    @OneToMany(mappedBy = "section", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<Question> questions;
}


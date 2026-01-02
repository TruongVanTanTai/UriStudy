package com.tantai.uristudy.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "Answer")
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", columnDefinition = "BIGINT", nullable = false)
    Long id;

    @Column(name = "IsCorrect", columnDefinition = "BIT", nullable = false)
    Boolean isCorrect;

    @Column(name = "AnswerContent", columnDefinition = "NVARCHAR(MAX)", nullable = false)
    String answerContent;

    @Column(name = "Image", columnDefinition = "NVARCHAR(1000)")
    String image;

    @ManyToOne
    @JoinColumn(name = "QuestionId", nullable = false)
    Question question;
}

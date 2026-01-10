package com.tantai.uristudy.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "ExamOfUser")
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class ExamOfUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", columnDefinition = "BIGINT", nullable = false)
    Long id;

    @Column(name = "NumberOfTimesPerformed", columnDefinition = "INT", nullable = false)
    Integer numberOfTimesPerformed;

    @ManyToOne
    @JoinColumn(name = "UserId", nullable = false)
    User user;

    @ManyToOne
    @JoinColumn(name = "ExamId", nullable = false)
    Exam exam;
}

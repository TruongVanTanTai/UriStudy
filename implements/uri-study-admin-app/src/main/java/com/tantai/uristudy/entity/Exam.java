package com.tantai.uristudy.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Exam")
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class Exam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", columnDefinition = "BIGINT", nullable = false)
    Long id;

    @Column(name = "Title", columnDefinition = "NVARCHAR(1000)", length = 1000, nullable = false)
    String title;

    @Column(name = "Image", columnDefinition = "NVARCHAR(1000)", length = 1000, nullable = false)
    String image;

    @Column(name = "ExamLevel", columnDefinition = "VARCHAR(50)", length = 50, nullable = false)
    String examLevel;

    @Column(name = "Description", columnDefinition = "NVARCHAR(3000)", length = 3000, nullable = false)
    String description;

    @Column(name = "Price", columnDefinition = "BIGINT", nullable = false)
    Long price;

    @Column(name = "Duration", columnDefinition = "INT", nullable = false)
    Integer duration;

    @Column(name = "ModifiedDate", columnDefinition = "DATETIME", nullable = false)
    LocalDateTime modifiedDate;

    @OneToMany(mappedBy = "exam", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<ExamOfUser> examOfUsers;

    @OneToMany(mappedBy = "exam", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<Section> sections;

    @OneToMany(mappedBy = "exam", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<BillDetail> billDetails;
}

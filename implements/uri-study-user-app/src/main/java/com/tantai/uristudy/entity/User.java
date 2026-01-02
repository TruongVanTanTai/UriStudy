package com.tantai.uristudy.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "User")
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", columnDefinition = "BIGINT", nullable = false)
    Long id;

    @Column(name = "Name", columnDefinition = "NVARCHAR(100)", length = 100, nullable = false)
    String name;

    @Column(name = "Email", columnDefinition = "NVARCHAR(100)", length = 100, unique = true, nullable = false)
    String email;

    @Column(name = "PhoneNumber", columnDefinition = "NVARCHAR(50)", length = 50, nullable = false)
    String phoneNumber;

    @Column(name = "DateOfBirth", columnDefinition = "DATE", nullable = false)
    LocalDate dateOfBirth;

    @Column(name = "Gender", columnDefinition = "BIT", nullable = false)
    Boolean gender;

    @Column(name = "Address", columnDefinition = "NVARCHAR(100)", length = 100, nullable = false)
    String address;

    @Column(name = "Username", columnDefinition = "NVARCHAR(100)", length = 100, unique = true, nullable = false)
    String username;

    @Column(name = "Password", columnDefinition = "NVARCHAR(100)", length = 100, nullable = false)
    String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<FlashCartSet> flashCartSets;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<FlashCartSetReview> flashCartSetReviews;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<ExamOfUser> examOfUsers;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<Bill> bills;
}


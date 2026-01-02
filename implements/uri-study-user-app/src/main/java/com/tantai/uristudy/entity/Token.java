package com.tantai.uristudy.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDateTime;

@Entity
@Table(name = "Token")
@DynamicInsert
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", columnDefinition = "BIGINT", nullable = false)
    Long id;

    @Column(name = "Token", columnDefinition = "UNIQUEIDENTIFIER", nullable = false)
    String token;

    @Column(name = "Email", columnDefinition = "NVARCHAR(100)", nullable = false)
    String email;

    @Column(name = "ExpirationTime", columnDefinition = "DATETIME", nullable = false)
    LocalDateTime expirationTime;

    @Column(name = "IsVerified", columnDefinition = "BIT", nullable = false)
    Boolean isVerified;
}

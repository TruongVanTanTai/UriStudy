package com.tantai.uristudy.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Bill")
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", columnDefinition = "BIGINT", nullable = false)
    Long id;

    @Column(name = "CreationDate", columnDefinition = "DATETIME", nullable = false)
    LocalDateTime creationDate;

    @Column(name = "IsPurchased", columnDefinition = "BIT", nullable = false)
    Boolean isPurchased;

    @ManyToOne
    @JoinColumn(name = "UserId", nullable = false)
    User user;

    @OneToMany(mappedBy = "bill", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<BillDetail> billDetails;
}

package com.example.mall.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "business_requests")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class BusinessRequestEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(nullable = false)
    private String username;

    @Setter
    @Column(nullable = false)
    private String businessNumber;

    @Setter
    @Column
    private String requestStatus;
}

package com.example.mall.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@Entity
@Table(name = "shops")
@NoArgsConstructor
@AllArgsConstructor
public class ShopEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column
    private String name;

    @Setter
    @Column
    private String description;

    @Setter
    @Column
    private String status;

    @Setter
    @Column
    private String reasonForRejection;

    @Setter
    @Column
    private String reasonForClosure;

    @Setter
    @Column
    private String category;

    // 쇼핑몰 소유자 아이디
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private UserEntity owner;
}

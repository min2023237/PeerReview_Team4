package com.example.mall.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ID
    @Setter
    @Column(nullable = false, unique = true)
    private String username;

    @Setter
    @Column
    private String password;

    // 이름
    @Setter
    @Column
    private String name;

    @Setter
    @Column
    private String nickname;

    @Setter
    @Column
    private String email;

    @Setter
    @Column
    private String phoneNumber;

    @Setter
    @Column
    private String ageGroup;

    @Setter
    @Column
    private String role;

    @Setter
    @Column
    private String businessNumber;

    @Setter
    @Column
    private String imageUrl;

    @OneToOne(mappedBy = "owner", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = true)
    private ShopEntity shop;


}

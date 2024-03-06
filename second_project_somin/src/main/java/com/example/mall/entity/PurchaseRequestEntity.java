package com.example.mall.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@Entity
@Table(name = "purchase_request")
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseRequestEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long productId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column
    private Double passedPrice;

    @Column
    private Long quantity;

    @Enumerated(EnumType.STRING)
    private PurchaseRequestEntity.Status status;

    public enum Status {
        REQUESTED, COMPLETED, CANCELED
    }
}

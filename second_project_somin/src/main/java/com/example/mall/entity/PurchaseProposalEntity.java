package com.example.mall.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@Entity
@Table(name = "purchase_proposal")
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseProposalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long itemId;

    //@Column
    //private Long userId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user; // 사용자 엔티티와의 연관 관계

    @Enumerated(EnumType.STRING)
    private Status status;

    public enum Status {
        PROPOSED, ACCEPTED, REJECTED, CONFIRMED
    }
}

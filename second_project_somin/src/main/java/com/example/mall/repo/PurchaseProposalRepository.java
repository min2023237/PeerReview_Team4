package com.example.mall.repo;


import com.example.mall.entity.PurchaseProposalEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PurchaseProposalRepository
        extends JpaRepository<PurchaseProposalEntity, Long> {
    List<PurchaseProposalEntity> findByItemId(Long itemId);
    List<PurchaseProposalEntity> findByUserIdAndItemId(Long userId, Long itemId);
}

package com.example.mall.repo;

import com.example.mall.entity.PurchaseRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PurchaseRequestRepository
        extends JpaRepository<PurchaseRequestEntity, Long> {
    List<PurchaseRequestEntity> findByProductId(Long proudctId);
    List<PurchaseRequestEntity> findByUserIdAndProductId(Long userId, Long productId);

    List<PurchaseRequestEntity> findByProductIdAndStatus(Long productId, PurchaseRequestEntity.Status status);

}

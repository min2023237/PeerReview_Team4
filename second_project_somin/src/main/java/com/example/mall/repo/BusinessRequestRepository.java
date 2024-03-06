package com.example.mall.repo;

import com.example.mall.entity.BusinessRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BusinessRequestRepository
        extends JpaRepository<BusinessRequestEntity, Long> {
    List<BusinessRequestEntity> findByRequestStatus(String requestStatus);
}

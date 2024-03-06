package com.example.mall.repo;

import com.example.mall.entity.ItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ItemRepository
        extends JpaRepository<ItemEntity, Long> {
}

package com.example.Mission_shop.repo;

import com.example.Mission_shop.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);
    boolean existsByUsername(String username);

    List<UserEntity> findByApply(String apply);
    Optional<UserEntity> findByBusinessNumber(String businessNumber);

    Optional<UserEntity> findIdByUsername(String username);
}

package com.example.jpaproject.repo;

import com.example.jpaproject.entity.ArticleEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleRepository
        extends JpaRepository<ArticleEntity, Long> {
    List<ArticleEntity> findAllByBoardId(Long id, Sort sort);
}

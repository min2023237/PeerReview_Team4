package com.example.jpaproject.repo;

import com.example.jpaproject.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository
        extends JpaRepository<CommentEntity, Long> {
    List<CommentEntity> findAllByArticleId(Long articleId);
}

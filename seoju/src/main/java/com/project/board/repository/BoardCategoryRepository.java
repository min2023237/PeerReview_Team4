package com.project.board.repository;

import com.project.board.entity.Board;
import com.project.board.entity.BoardCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BoardCategoryRepository extends JpaRepository<BoardCategory, Integer> {
    Optional<BoardCategory> findById(int id);

    default String findCategoryNameById(Integer categoryId) {
        Optional<BoardCategory> categoryOptional = findById(categoryId);
        return categoryOptional.map(BoardCategory::getCategoryname).orElse(null);
    }
}
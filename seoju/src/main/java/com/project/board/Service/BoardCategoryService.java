package com.project.board.Service;

import com.project.board.entity.BoardCategory;
import com.project.board.repository.BoardCategoryRepository;
import com.project.board.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BoardCategoryService {
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private BoardCategoryRepository boardCategoryRepository;

    // 카테고리 이름 가져오기
    public String categoryName(int id) {
        return boardCategoryRepository.findCategoryNameById(id);
    }

    // 카테고리 목록 조회
    public List<BoardCategory> categoriList() {
        return boardCategoryRepository.findAll();
    }

}

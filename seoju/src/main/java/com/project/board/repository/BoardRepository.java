package com.project.board.repository;

import com.project.board.entity.Board;
import com.project.board.entity.BoardCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository

public interface BoardRepository extends JpaRepository<Board, Integer> {
    // JPA Repository에서는 검색기능을 제공
    // findBy(컬럼 이름) : 컬럼에서 키워드를 넣어서 찾겠다 :: 정확하게 일치하는 데이터만 검색
    // findBy(컬럼 이름) Containing : 컬럼에서 키워드가 포함된 것을 찾겠다 :: 키워드가 포함된 모든 데이터 검색

    // 게시글 아이디 찾기
    Optional<Board> findById(int id);
    default String findBoardTitleById(Integer boardId) {
        Optional<Board> boardOptional = findById(boardId);
        return boardOptional.map(Board::getTitle).orElse(null);
    }
    // 전체 게시글 찾기
    Page<Board> findByTitleContaining(String searchKeyword, Pageable pageable);
    Page<Board> findByContentContaining(String searchKeyword, Pageable pageable);
    // 카테고리 게시글 조회
    Page<Board> findByBoardCategory_Id(Integer categoryId, Pageable pageable);
    // 카테고리 내 게시글 찾기
    Page<Board> findByBoardCategory_IdAndTitleContaining(Integer categoryId, String keyword, Pageable pageable);
    Page<Board> findByBoardCategory_IdAndContentContaining(Integer categoryId, String keyword, Pageable pageable);

}

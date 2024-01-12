package com.project.board.Service;

import com.project.board.entity.Board;
import com.project.board.entity.BoardCategory;
import com.project.board.repository.BoardCategoryRepository;
import com.project.board.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class BoardService {
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private BoardCategoryRepository boardCategoryRepository;


    //글 작성
    public void writeService(Board board, MultipartFile file, Integer categoryid) throws IOException {
        if (file != null && !file.isEmpty()) { // 파일이 첨부되었을 때만 처리
            String projectPath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\files"; // 저장할 경로를 지정
            UUID uuid = UUID.randomUUID(); // 파일이름에 붙일 랜덤 이름 생성
            String fileName = uuid + "_" + file.getOriginalFilename(); // 랜덤 이름 + 언더바 + 원래파일이름
            File saveFile = new File(projectPath, fileName); // 파일을 생성해주는데 projectPath 경로에 넣고, 이름은 fileName으로 담김
            file.transferTo(saveFile);
            board.setFilename(fileName);
            board.setFilepath("/files/" + fileName);
        } else {

        }

        BoardCategory categoryId = boardCategoryRepository.findById(categoryid).orElse(null);
        board.setBoardCategory(categoryId);
        boardRepository.save(board);
    }

    //전체 게시글 리스트
    public Page<Board> boardList(Pageable pageable) {
        return boardRepository.findAll(pageable);
    }

    // 카테고리별 게시글 리스트
    public Page<Board> boardListByCategory(Integer categoryId, Pageable pageable) {
        return boardRepository.findByBoardCategory_Id(categoryId, pageable);
    }


    // 단일 게시물 조회
    public Board getContentById(Integer id) {
        return boardRepository.findById(id).get();
    }


    // 단일 게시글 삭제 시 비밀번호를 체크
    public boolean checkPassword(Integer id, String password) {
        Board board = boardRepository.findById(id).get();
        String storedPassword = board.getPassword(); // 저장된 비밀번호 가져오기

        // 비밀번호 일치 여부 확인
        return storedPassword.equals(password);
    }

    // 단일 게시글 삭제
    public void boardDelete(Integer id) {
        boardRepository.deleteById(id);
    }

    // 게시글 검색
    public Page<Board> boardSearchList(String searchType, String searchKeyword, Pageable pageable) {
        if (searchType.equals("title")) {
            return boardRepository.findByTitleContaining(searchKeyword, pageable);
        } else {
            return boardRepository.findByContentContaining(searchKeyword, pageable);
        }
    }

    // 카테고리 게시글 검색
    public Page<Board> boardSearchListByCategory(Integer categoryId, String searchType, String searchKeyword, Pageable pageable) {
        if (searchType.equals("title")) {
            return boardRepository.findByBoardCategory_IdAndTitleContaining(categoryId, searchKeyword, pageable);
        } else {
            return boardRepository.findByBoardCategory_IdAndContentContaining(categoryId, searchKeyword, pageable);
        }
    }
}

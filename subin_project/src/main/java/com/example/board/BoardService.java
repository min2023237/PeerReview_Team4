package com.example.board;

import com.example.board.dto.BoardDto;
import com.example.board.entity.Board;
import com.example.board.repo.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    public List<BoardDto> readAll(){
        List<BoardDto> boards = new ArrayList<>();
        for (Board board: boardRepository.findAll())
            boards.add(BoardDto.fromEntity(board));
        return boards;
    }

    public BoardDto read(Long id){
        Board board = boardRepository.findById(id).orElseThrow();
        return BoardDto.fromEntity(board);
    }
}

package com.example.jpaproject;

import com.example.jpaproject.entity.BoardEntity;
import com.example.jpaproject.repo.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository repository;

    //CREATE BOARD
    public void create(
            String name
    ) {
        BoardEntity board = new BoardEntity();
        board.setName(name);
        repository.save(board);
    }

    //READ ALL
    public List<BoardEntity> readBoardAll() {
        return repository.findAll();
    }

    //READ ONE
    public BoardEntity readBoard(Long id) {
        return repository.findById(id).orElse(null);
    }


}

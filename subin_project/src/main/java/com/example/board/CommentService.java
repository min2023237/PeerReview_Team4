package com.example.board;

import com.example.board.dto.CommentDto;
import com.example.board.entity.Article;
import com.example.board.entity.Board;
import com.example.board.entity.Comment;
import com.example.board.repo.ArticleRepository;
import com.example.board.repo.BoardRepository;
import com.example.board.repo.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;


    public CommentDto create(Long boardId, Long articleId, CommentDto dto) {
        Board board = boardRepository.findById(boardId).orElseThrow();
        Article article = articleRepository.findById(articleId)
                .orElseThrow();
        Comment comment = new Comment(
                dto.getContent(),
                dto.getOriginPassword(),
                article,
                board
        );
        return CommentDto.fromEntity(commentRepository.save(comment));
    }

    public CommentDto read(Long commentId) {
        return CommentDto.fromEntity(commentRepository.findById(commentId)
                .orElseThrow());
    }

    public CommentDto update(Long commentId, CommentDto dto) {
        Comment comment = commentRepository.findById(commentId).orElseThrow();
        comment.setContent(dto.getContent());
        comment.setOriginPassword(dto.getOriginPassword());
        return CommentDto.fromEntity(commentRepository.save(comment));
    }

    public void delete(Long commentId, CommentDto dto) {
        Comment comment = commentRepository.findById(commentId).orElseThrow();
        comment.setContent(dto.getContent());
        comment.setOriginPassword(dto.getOriginPassword());
        commentRepository.delete(comment);

    }
}

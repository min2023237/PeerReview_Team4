package com.project.board.Service;

import com.project.board.entity.Board;
import com.project.board.entity.BoardCategory;
import com.project.board.entity.Comment;
import com.project.board.repository.BoardRepository;
import com.project.board.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private BoardRepository boardRepository;

    // 댓글 작성
    public void writeComment(Comment comment, Integer boardid) {
        Optional<Board> optionalBoardId = boardRepository.findById(boardid);
        Board boardId = optionalBoardId.get();
        comment.setBoard(boardId);
        commentRepository.save(comment);
    }

    //id로 comment 불러오기
    public Comment commentView(Integer id) {
        return commentRepository.findById(id).get();
    }




    // 단일 댓글 삭제 시 비밀번호를 체크
    public boolean checkPassword(Integer id, String password) {
        Comment comment = commentRepository.findById(id).get();
        String storedPassword = comment.getCommentpw(); // 저장된 비밀번호 가져오기

        // 비밀번호 일치 여부 확인
        return storedPassword.equals(password);
    }

    // 댓글 삭제
    public void deleteComment(Integer commentId) {
        commentRepository.deleteById(commentId);
    }
}

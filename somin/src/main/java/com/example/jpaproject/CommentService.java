package com.example.jpaproject;

import com.example.jpaproject.entity.CommentEntity;
import com.example.jpaproject.repo.ArticleRepository;
import com.example.jpaproject.repo.CommentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {
    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;

    public CommentService(
            ArticleRepository articleRepository,
            CommentRepository commentRepository
    ) {
        this.articleRepository = articleRepository;
        this.commentRepository = commentRepository;
    }

    //CREATE COMMENT
    public void create(
            String content,
            String password,
            Long articleId
    ) {
        CommentEntity comment = new CommentEntity();
        comment.setContent(content);
        comment.setPassword(password);
        comment.setArticle(articleRepository.findById(articleId).orElse(null));
        commentRepository.save(comment);
    }

    // READ ALL
    public List<CommentEntity> readAll(
            Long articleId
    ) {
        return commentRepository.findAllByArticleId(articleId);
    }

    // DELETE
    public void delete(
            Long articleId,
            String password
    ) {
        Optional<CommentEntity> targetOptional = commentRepository.findById(articleId);
        CommentEntity target = targetOptional.get();
        if(targetOptional.isPresent() && target.getPassword().equals(password)) {
            commentRepository.delete(target);
        }
    }

    public void deleteAll(
            Long articleId
    ) {
        List<CommentEntity> target = commentRepository.findAllByArticleId(articleId);
        commentRepository.deleteAll(target);
    }

}

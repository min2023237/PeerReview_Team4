package com.example.jpaproject;

import com.example.jpaproject.entity.ArticleEntity;
import com.example.jpaproject.repo.ArticleRepository;
import com.example.jpaproject.repo.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
//@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final BoardRepository boardRepository;

    public ArticleService(
            ArticleRepository articleRepository,
            BoardRepository boardRepository
    ) {
        this.articleRepository = articleRepository;
        this.boardRepository = boardRepository;
    }

    //Long nextId = 1L;

    //CREATE ARTICLE
    public void create(
            String title,
            String content,
            String pwd,
            Long boardId
    ) {
        ArticleEntity article = new ArticleEntity();

        article.setTitle(title);
        article.setContent(content);
        article.setPwd(pwd);
        article.setBoard(boardRepository.findById(boardId).orElse(null));

        articleRepository.save(article);

    }

    //READ ALL - boardId
    public List<ArticleEntity> readAllByBoardId(
            Long boardId
    ) {
        return articleRepository.findAllByBoardId(boardId, Sort.by(Sort.Order.desc("id")));
    }

    //READ ALL
    public List<ArticleEntity> readAll() {
        return articleRepository.findAll(Sort.by(Sort.Order.desc("id")));
    }

    //READ ONE
    public ArticleEntity readOne(
            Long id
    ) {
        Optional<ArticleEntity> optionalArticle = articleRepository.findById(id);
        return optionalArticle.orElse(null);
    }

    //UPDATE ARTICLE
    public void update(
            Long id,
            String title,
            String content,
            String pwd
    ) {
        ArticleEntity target = readOne(id);
        target.setTitle(title);
        target.setContent(content);
        if(target.getPwd().equals(pwd)) {
            articleRepository.save(target);
        }

    }

    //DELETE ARTICLE
    public void delete(
            Long id,
            String pwd
    ) {
        Optional<ArticleEntity> targetOptional = articleRepository.findById(id);
        ArticleEntity target = targetOptional.get();
        if(targetOptional.isPresent() && target.getPwd().equals(pwd)) {
            articleRepository.delete(target);
        }

    }

}

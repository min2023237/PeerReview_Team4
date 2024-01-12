package com.example.board;

import com.example.board.dto.ArticleDto;
import com.example.board.entity.Article;
import com.example.board.entity.Board;
import com.example.board.repo.ArticleRepository;
import com.example.board.repo.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final BoardRepository boardRepository;

    // 게시글 만들기
    public ArticleDto create(Long boardId,ArticleDto dto){
        Board board = boardRepository.findById(boardId).orElseThrow();
        Article article = new Article(
                dto.getTitle(),
                dto.getContent(),
                dto.getOriginPassword(),
                board
        );
        // fromEntity -> entity를 dto로 변환
        return ArticleDto.fromEntity(articleRepository.save(article));
    }

    // 게시글 모두 읽기
    public List<ArticleDto> readAll(){
        List<ArticleDto> articleList = new ArrayList<>();
        for (Article article : articleRepository.findAll()){
            articleList.add(ArticleDto.fromEntity(article));
        }
        return articleList;
    }

    // 게시글 하나 읽기
    public ArticleDto readOne(Long id){
        Article article = articleRepository.findById(id).orElseThrow();
        return ArticleDto.fromEntity(article);
    }

    // 게시글 수정
    public ArticleDto update(Long id, ArticleDto dto){
        Article article = articleRepository.findById(id).orElseThrow();
        article.setTitle(dto.getTitle());
        article.setContent(dto.getContent());
        article.setOriginPassword(dto.getOriginPassword());
        return ArticleDto.fromEntity(articleRepository.save(article));
    }

    // 게시글 삭제
    public void delete(Long id){
        articleRepository.delete(articleRepository.findById(id).orElseThrow());
    }
}

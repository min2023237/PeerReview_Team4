package com.example.board.dto;

import com.example.board.entity.Article;
import com.example.board.entity.Board;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class BoardDto {
    private Long id;

    @Setter
    private String boardName;

    private List<ArticleDto> articles = new ArrayList<>();

    public BoardDto(Long id, String boardName) {
        this.id = id;
        this.boardName = boardName;
    }

    public static BoardDto fromEntity(Board entity){
        BoardDto dto = new BoardDto();
        dto.id = entity.getId();
        dto.boardName = entity.getBoardName();
        dto.articles = new ArrayList<>();
        for (Article article: entity.getArticles())
            dto.articles.add(ArticleDto.fromEntity(article));
        return dto;
    }
}

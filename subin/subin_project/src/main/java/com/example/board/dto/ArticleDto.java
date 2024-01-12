package com.example.board.dto;

import com.example.board.entity.Article;
import com.example.board.entity.Comment;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
@NoArgsConstructor
public class ArticleDto {
    private Long id;
    @Setter
    private String title;
    @Setter
    private String content;
    @Setter
    private String originPassword;

    private List<CommentDto> comments = new ArrayList<>();

    public ArticleDto(String title, String content, String originPassword) {
        this.title = title;
        this.content = content;
        this.originPassword = originPassword;
    }


    public static ArticleDto fromEntity(Article entity) {
        ArticleDto dto = new ArticleDto();
        dto.id = entity.getId();
        dto.title = entity.getTitle();
        dto.content = entity.getContent().replace("\n", "<br>");
        dto.originPassword = entity.getOriginPassword();
        dto.comments = new ArrayList<>();
        for (Comment comment: entity.getComments())
            dto.comments.add(CommentDto.fromEntity(comment));
        return dto;
    }

}

package com.example.board.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    private String content;
    @Setter
    private String originPassword;

    @Setter
    @ManyToOne
    private Article article;

    @ManyToOne
    private Board board;

    public Comment() {
    }

    public Comment(String content, String originPassword, Article article, Board board) {
        this.content = content;
        this.originPassword = originPassword;
        this.article = article;
        this.board = board;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", articleId=" + article.getId() +
                ", boardId=" + board.getId() +
                ", content='" + content + '\'' +
                ", originPassword='" + originPassword + '\'' +
                '}';
    }
}

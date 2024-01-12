package com.example.jpaproject.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;
    private String password;

    //댓글은 하나의 게시글에 속함
    @ManyToOne
    private ArticleEntity article;

}

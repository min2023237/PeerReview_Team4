package com.example.jpaproject.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class ArticleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;
    private String pwd;

    //게시글은 하나의 게시판에 속함
    @ManyToOne
    private BoardEntity board;


}

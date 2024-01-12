package com.project.board.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity //테이블을 의미!
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Board {

    @Id // pk
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // 아이디
    private Integer id;

    // 게시글 내용
    private String title;
    private String content;
    private String password;
    private String filename;
    private String filepath;

    // 게시판
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "categoryid")
    private BoardCategory boardCategory;

    // 게시글과 댓글 간의 양방향 관계 설정
    @OneToMany(mappedBy = "board", fetch = FetchType.EAGER) // mappedBy : 연관관계 주인이 아님 == FK가 아님 == DB에 컬럼 안만듬
    private List<Comment> comments;
}
package com.project.board.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "categoryid")  // 실제 DB 테이블의 컬럼명 지정
    private Integer id;

    private String categoryname;

    // 게시판과 게시글 간의 양방향 관계 설정
    @OneToMany(mappedBy = "boardCategory")
    private List<Board> board;

}
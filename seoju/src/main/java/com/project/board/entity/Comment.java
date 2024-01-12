package com.project.board.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "comment")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "commentid", nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer id;

    private String commentname;
    private String commentpw;
    private String commentcontent;

    // 게시글과 관계
    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    @CreationTimestamp
    private Timestamp createDate;

}

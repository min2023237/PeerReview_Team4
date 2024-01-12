package com.example.board;

import com.example.board.dto.ArticleDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("board/{boardId}/article")
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;
    private final BoardService boardService;

    // 하나의 게시판의 게시글 목록을 전부 보기
    @GetMapping
    public String article(
            @PathVariable("boardId")
            Long boardId,
            Model model){
        model.addAttribute("articles",articleService.readAll());
        model.addAttribute("boardId",boardId);
        return String.format("board/%d/article/index", boardId);
    }

    @GetMapping("write")
    public String articleWrite(
            @PathVariable("boardId")
            Long boardId,
            Model model
    ) {
        model.addAttribute("boards", boardService.readAll());
        model.addAttribute("boardId",boardId);
        return "board/article/write";
    }

    // 게시글 만들기
    @PostMapping
    public String articleCreate(
            @PathVariable("boardId")
            Long boardId,
            @RequestParam("title")
            String title,
            @RequestParam("content")
            String content,
            @RequestParam("originPassword")
            String originPassword
    ){
        articleService.create(boardId,new ArticleDto(title, content, originPassword));
        return  String.format("redirect:/board/%d", boardId);
    }


    // 게시글 하나만 읽기
    @GetMapping("{id}")
    public String articleReadOne(
            @PathVariable("boardId")
            Long boardId,
            @PathVariable("id")
            Long id,
            Model model
    ){
        model.addAttribute("boardId",boardId);
        model.addAttribute("article", articleService.readOne(id));
        return "board/article/read";
    }

    // 수정 페이지 보여주기
    @GetMapping("{id}/edit")
    public String articleEdit(
            @PathVariable("boardId")
            Long boardId,
            @PathVariable("id")
            Long id,
            Model model
    ){
        model.addAttribute("boardId",boardId);
        model.addAttribute("article",articleService.readOne(id));
        return "board/article/edit";
    }

    @PostMapping("{id}/update")
    public String articleUpdate(
            @PathVariable("boardId")
            Long boardId,
            @PathVariable("id")
            Long id,
            @RequestParam("title")
            String title,
            @RequestParam("content")
            String content,
            @RequestParam("originPassword")
            String originPassword,
            Model model

    ){
        model.addAttribute("boardId",boardId);
        // 비밀번호 일치하면 수정한 내용 확인을 위해 게시글 단일 조회 페이지 이동
        articleService.update(id, new ArticleDto(title, content,originPassword));
        return  String.format("redirect:/board/%d", boardId);
    }

    @GetMapping("{id}/delete-view")
    public String articleDeleteView(
            @PathVariable("boardId")
            Long boardId,
            @PathVariable("id")
            Long id,
            Model model
    ){
        model.addAttribute("boardId",boardId);
        model.addAttribute("article",articleService.readOne(id));
        return "board/article/delete";
    }
    @PostMapping("{id}/delete")
    public String articleDelete(
            @PathVariable("boardId")
            Long boardId,
            @PathVariable("id")
            Long id
    ){
        // 비밀번호 일치하면 수정한 게시글이 삭제되므로 게시글 전체 조회 페이지 이동
        articleService.delete(id);
        return  String.format("redirect:/board/%d", boardId);
    }


}

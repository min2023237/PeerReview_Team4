package com.example.board;

import com.example.board.dto.CommentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("board/{boardId}/article/{articleId}/comment")
@RequiredArgsConstructor
public class CommentController {
    private final ArticleService articleService;
    private final CommentService commentService;

    @PostMapping
    public String create(
            @PathVariable("boardId")
            Long boardId,
            @PathVariable("articleId")
            Long articleId,
            @RequestParam("originPassword")
            String originPassword,
            @RequestParam("content")
            String content
    ) {
        commentService.create(boardId, articleId, new CommentDto(content, originPassword));
        return String.format("redirect:/board/%d/article/%d", boardId,articleId);
    }

    @GetMapping("{commentId}/edit")
    public String edit(
            @PathVariable("boardId")
            Long boardId,
            @PathVariable("articleId")
            Long articleId,
            @PathVariable("commentId")
            Long commentId,
            Model model
    ) {
        model.addAttribute("boardId",boardId);
        model.addAttribute("article", articleService.readOne(articleId));
        model.addAttribute("comment", commentService.read(commentId));
        return "board/article/comment/edit";
    }

    @PostMapping("{commentId}/commentUpdate")
    public String update(
            @PathVariable("boardId")
            Long boardId,
            @PathVariable("articleId")
            Long articleId,
            @PathVariable("commentId")
            Long commentId,
            @RequestParam("content")
            String content,
            @RequestParam("originPassword")
            String originPassword,
            Model model
    ) {
        model.addAttribute("boardId",boardId);
        commentService.update(commentId, new CommentDto(content,originPassword));
        return String.format("redirect:/board/%d/article/%d", boardId, articleId);
    }

    @GetMapping("{commentId}/delete")
    public String commentDeleteView(
            @PathVariable("boardId")
            Long boardId,
            @PathVariable("articleId")
            Long articleId,
            @PathVariable("commentId")
            Long commentId,
            Model model
    ) {
        model.addAttribute("boardId",boardId);
        model.addAttribute("article", articleService.readOne(articleId));
        model.addAttribute("comment", commentService.read(commentId));
        return "board/article/comment/delete";
    }

    @PostMapping("{commentId}/commentDelete")
    public String delete(
            @PathVariable("boardId")
            Long boardId,
            @PathVariable("articleId")
            Long articleId,
            @PathVariable("commentId")
            Long commentId,
            @RequestParam("content")
            String content,
            @RequestParam("originPassword")
            String originPassword
    ) {
        commentService.delete(commentId, new CommentDto(content,originPassword));
        return String.format("redirect:/board/%d/article/%d",boardId ,articleId);
    }


}

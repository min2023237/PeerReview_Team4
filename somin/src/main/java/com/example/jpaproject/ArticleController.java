package com.example.jpaproject;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class ArticleController {

    //private final ArticleRepository repository;
    private final ArticleService articleService;
    private final BoardService boardService;
    private final CommentService commentService;


    //CREATE VIEW
    @GetMapping("/create-view/{id}")
    public String creatView(
            @PathVariable
            Long id,
            Model model
    ) {
        model.addAttribute("boards", boardService.readBoardAll());
        model.addAttribute("boardId", id);
        return "article/create";
    }

    //CREATE ARTICLE
    @PostMapping("/create")
    public String create(
            @RequestParam("title")
            String title,
            @RequestParam("content")
            String content,
            @RequestParam("pwd")
            String pwd,
            @RequestParam("board-id")
            Long boardId
    ) {
        articleService.create(title, content, pwd, boardId);

        return "redirect:/boards/"+boardId;
    }

    //CREATE COMMENT
    @PostMapping("/article/{id}/comment")
    public String createComment(
            @PathVariable
            Long id,
            @RequestParam
            String content,
            @RequestParam
            String password
    ) {
        commentService.create(content, password, id);
        return "redirect:/article/" + id;
    }

    //READ ONE - ARTICLE
    @GetMapping("/article/{id}")
    public String readOne(
            @PathVariable("id")
            Long id,
            Model model
    ) {
        model.addAttribute("articles", articleService.readOne(id));
        model.addAttribute("comments", commentService.readAll(id));
        return "article/read";
    }



    //UPDATE VIEW
    @GetMapping("/update-view/{id}")
    public String updateView(
            @PathVariable("id")
            Long id,
            Model model
    ) {
        model.addAttribute("articles", articleService.readOne(id));
        return "article/update";
    }
    //UPDATE ARTICLE
    @PostMapping("/article/{id}/update")
    public String update(
            @PathVariable("id")
            Long id,
            @RequestParam("title")
            String title,
            @RequestParam("content")
            String content,
            @RequestParam("pwd")
            String pwd
    ) {
            articleService.update(id, title, content, pwd);
            return String.format("redirect:/article/%d", id);
    }

    //DELETE ARTICLE
    @PostMapping("/article/{id}/delete")
    public String delete(
            @PathVariable("id")
            Long id,
            @RequestParam("pwd")
            String pwd
    ) {
        Long boardId = articleService.readOne(id).getBoard().getId();
        articleService.delete(id, pwd);
        commentService.deleteAll(id);

        return "redirect:/boards/"+boardId;
    }

    //DELETE COMMENT
    @PostMapping("/article/{id}/comment/{commentId}/delete")
    public String deleteComment(
            @PathVariable("id")
            Long id,
            @PathVariable("commentId")
            Long commentId,
            @RequestParam("password")
            String password
    ) {
        commentService.delete(commentId, password);
        return "redirect:/article/" + id;
    }

}

package com.example.jpaproject;

import com.example.jpaproject.entity.BoardEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;
    private final ArticleService articleService;

    @GetMapping("/")
    public String home() {
        return "home";
    }

    //CREATE
    @GetMapping("board/{name}/create")
    public String create(
            @PathVariable("name")
            String name
    ) {

        boardService.create(name);

        return "redirect:/boards";

    }

    //READ ALL
    @GetMapping("boards")
    public String readAll(Model model) {
        model.addAttribute("boards", boardService.readBoardAll());
        return "board/home";
    }

    //READ ONE
    @GetMapping("boards/{id}")
    public String readOne(
            @PathVariable("id")
            Long id,
            Model model
    ) {
        model.addAttribute("board", boardService.readBoard(id));
        model.addAttribute("articles", articleService.readAllByBoardId(id));
        model.addAttribute("allArticles", articleService.readAll());
        return "board/read";
    }


}

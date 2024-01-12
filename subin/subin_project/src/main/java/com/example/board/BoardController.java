package com.example.board;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("board")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;
    private final ArticleService articleService;

    @GetMapping
    public String readAll(Model model){
        model.addAttribute("boards",boardService.readAll());
        return "board/index";
    }

    @GetMapping("article/index")
    public String readArticleAll(Model model){
        model.addAttribute("articles",articleService.readAll());
        return "board/article/index";
    }


    @GetMapping("{id}")
    public String read(
            @PathVariable("id")
            Long id,
            Model model
    ){
        model.addAttribute("board",boardService.read(id));
        return "board/read";
    }

}

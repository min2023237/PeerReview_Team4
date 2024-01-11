package com.example.jpa;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("instructor")
public class InstructorController {
    private final InstructorService instructorService;

    @GetMapping
    public String readAll(Model model) {
        model.addAttribute("instructors", instructorService.readInstructorAll());
        return "instructor/home";
    }

    @GetMapping("{id}")
    public String readOne(
            @PathVariable("id")
            Long id,
            Model model
    ) {
        model.addAttribute("instructor", instructorService.readInstructor(id));
        return "instructor/read";
    }
}

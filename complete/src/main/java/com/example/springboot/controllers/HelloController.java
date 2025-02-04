package com.example.springboot.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller  // Ensures it's a View Controller
public class HelloController {

    @GetMapping("/")
    public String home(Model model) {
        return "index";  // This maps to src/main/resources/templates/index.html
    }
}

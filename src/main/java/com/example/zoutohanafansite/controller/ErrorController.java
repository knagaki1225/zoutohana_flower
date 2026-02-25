package com.example.zoutohanafansite.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrorController {
    @GetMapping("/error-4xx")
    public String error4xx() {
        return "auth/404";
    }

}

package com.project.fitness.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PublicController {
    @GetMapping("/") public String home() {
        return "Fitness Tracker Backend is Live 🚀";
    }
}

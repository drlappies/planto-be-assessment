package com.planto.fullstackassessment.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api")
public class AppController {
    @GetMapping()
    public String index(HttpServletRequest request, HttpServletResponse response) {
        return "csv-editor-be";
    }
}

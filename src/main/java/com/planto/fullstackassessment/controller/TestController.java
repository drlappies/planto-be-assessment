package com.planto.fullstackassessment.controller;

import com.planto.fullstackassessment.model.TestEntity;
import com.planto.fullstackassessment.service.TestService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/v1/api")
@RequiredArgsConstructor
public class TestController {
    private final TestService testService;

    @GetMapping(value = "/test", produces = "application/json")
    @CrossOrigin(origins = {"http://localhost:3000"})
    public Map<String, String> test(HttpServletRequest request, HttpServletResponse response) {
        TestEntity test = testService.getTest();
        String val = test != null ? test.getValue() : "Hello, World!";
        return Collections.singletonMap("value", val);
    }

    @PostMapping(value = "/add", produces = "application/text")
    @CrossOrigin(origins = {"http://localhost:3000"})
    public String add(HttpServletRequest request, HttpServletResponse response) {
        testService.addTest();
        return "Added!";
    }
}

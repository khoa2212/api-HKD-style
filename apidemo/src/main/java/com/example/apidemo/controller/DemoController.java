package com.example.apidemo.controller;

import com.example.apidemo.service.DemoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {
    private final DemoService demoService;

    public DemoController(DemoService demoService) {
        this.demoService = demoService;
    }

    @GetMapping("/api/hello-world")
    public ResponseEntity<String> sendHelloWorld() {
        return ResponseEntity.ok(demoService.sendHelloWorld());
    }
}

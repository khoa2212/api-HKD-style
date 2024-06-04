package com.example.apidemo.service;

import org.springframework.stereotype.Service;

@Service
public class DemoService {

    public String sendHelloWorld() {
        return "Hello world";
    }
}

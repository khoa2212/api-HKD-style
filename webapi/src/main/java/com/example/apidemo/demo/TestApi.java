package com.example.apidemo.demo;

import com.example.apidemo.product.dto.ListProductResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestApi {
    @GetMapping(path = "/test")
    public void findPaginatedProducts() {
        System.out.println("TEST SUCCESS");
    }
}

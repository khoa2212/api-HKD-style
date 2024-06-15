package com.example.product_service.controller;

import com.example.product_service.dto.ListProductResponseDTO;
import com.example.product_service.dto.ProductDTO;
import com.example.product_service.exception.ProductNotFoundException;
import com.example.product_service.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProductController {

    @Autowired
    ProductService productService;

    @GetMapping(path = "/products")
    public ResponseEntity<ListProductResponseDTO> findPaginatedProducts(@RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        return ResponseEntity.ok().body(productService.findPaginatedProducts(pageNumber, pageSize));
    }

    @GetMapping(path = "/products/{id}")
    public ResponseEntity<ProductDTO> findPaginatedProducts(@PathVariable("id") String id) throws ProductNotFoundException {
        return ResponseEntity.ok().body(productService.findById(id));
    }
}

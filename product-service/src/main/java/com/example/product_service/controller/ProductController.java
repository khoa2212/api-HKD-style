package com.example.product_service.controller;

import com.example.product_service.dto.ProductDTO;
import com.example.product_service.entity.Product;
import com.example.product_service.mapper.ProductMapper;
import com.example.product_service.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProductController {
    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductMapper productMapper;

    @GetMapping(path = "/products")
    public ResponseEntity<List<ProductDTO>> getAll() {
        return ResponseEntity.ok(productMapper.toListProductDTO(productRepository.findAll()));
    }
}

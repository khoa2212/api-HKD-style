package com.example.apidemo.product.controller;

import com.example.apidemo.exception.ProductNotFoundException;
import com.example.apidemo.product.dto.ListProductResponseDTO;
import com.example.apidemo.product.dto.ProductDTO;
import com.example.apidemo.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {

    @Autowired
    ProductService productService;

    @GetMapping(path = "/api/products")
    public ResponseEntity<ListProductResponseDTO> findPaginatedProducts(@RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        return ResponseEntity.ok().body(productService.findPaginatedProducts(pageNumber, pageSize));
    }

    @GetMapping(path = "/api/products/{id}")
    public ResponseEntity<ProductDTO> findByStatusAndId(@PathVariable("id") String id) throws ProductNotFoundException {
        return ResponseEntity.ok().body(productService.findByStatusAndId(id));
    }

    @GetMapping(path = "/api/products/sales")
    public ResponseEntity<ListProductResponseDTO> findPaginatedSalesProducts(@RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        return ResponseEntity.ok().body(productService.findPaginatedSalesProducts(pageNumber, pageSize));
    }
}


package com.example.apidemo.product.controller;

import com.example.apidemo.exception.ProductNotFoundException;
import com.example.apidemo.product.dto.AddProductRequestDTO;
import com.example.apidemo.product.dto.ListProductResponseDTO;
import com.example.apidemo.product.dto.ProductDTO;
import com.example.apidemo.product.dto.UpdateProductRequestDTO;
import com.example.apidemo.product.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
public class ProductController {

    @Autowired
    ProductService productService;

    @GetMapping(path = "/products")
    public ResponseEntity<ListProductResponseDTO> findPaginatedProducts(@RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        return ResponseEntity.ok().body(productService.findPaginatedProducts(pageNumber, pageSize));
    }

    @GetMapping(path = "/products/{id}")
    public ResponseEntity<ProductDTO> findByStatusAndId(@PathVariable("id") String id) throws ProductNotFoundException {
        return ResponseEntity.ok().body(productService.findByStatusAndId(id));
    }

    @GetMapping(path = "/products/sales")
    public ResponseEntity<ListProductResponseDTO> findPaginatedSalesProducts(@RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        return ResponseEntity.ok().body(productService.findPaginatedSalesProducts(pageNumber, pageSize));
    }

    @PostMapping(path = "/products")
    public ResponseEntity<ProductDTO> add(@Valid @ModelAttribute AddProductRequestDTO addProductRequestDTO) throws IOException {
        return ResponseEntity.ok().body(productService.add(addProductRequestDTO));
    }

    @DeleteMapping(path = "/products/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") String id) throws ProductNotFoundException {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(path = "/products/{id}")
    public ResponseEntity<ProductDTO> update(@PathVariable("id") String id, @Valid @ModelAttribute UpdateProductRequestDTO updateProductRequestDTO) throws IOException, ProductNotFoundException {
        ProductDTO productDTO = productService.update(id, updateProductRequestDTO);
        return ResponseEntity.ok().body(productDTO);
    }
}


package com.example.product_service.service;

import com.example.product_service.dto.ListProductResponseDTO;
import com.example.product_service.dto.ProductDTO;
import com.example.product_service.entity.Product;
import com.example.product_service.exception.ProductNotFoundException;
import com.example.product_service.mapper.ProductMapper;
import com.example.product_service.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductMapper productMapper;

    // find sales product

    // find best selling product

    // find all with pagination
    public ListProductResponseDTO findPaginatedProducts(int pageNumber, int pageSize) {
        Page<Product> products = productRepository.findAll(PageRequest.of(pageNumber, pageSize));
        return ListProductResponseDTO.builder()
                .products(productMapper.toListProductDTO(products.getContent()))
                .first(products.isFirst())
                .last(products.isLast())
                .totalPages(products.getTotalPages())
                .totalElements(products.getTotalElements())
                .numberOfElements(products.getNumberOfElements())
                .build();
    }

    // find detail
    public ProductDTO findById(String id) throws ProductNotFoundException {
        Product product = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("Product not found", "productNotFound"));
        return productMapper.toProductDTO(product);
    }

    // role job make random 16 sales product
}

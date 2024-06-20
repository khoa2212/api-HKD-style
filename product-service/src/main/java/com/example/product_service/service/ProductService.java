package com.example.product_service.service;

import com.example.product_service.dto.ListProductResponseDTO;
import com.example.product_service.dto.ProductDTO;
import com.example.product_service.entity.Product;
import com.example.product_service.enums.StatusEnum;
import com.example.product_service.exception.ProductNotFoundException;
import com.example.product_service.mapper.ProductMapper;
import com.example.product_service.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    ProductRepository productRepository;

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    ProductMapper productMapper;

    // find sales product
    public ListProductResponseDTO findPaginatedSalesProducts(int pageNumber, int pageSize) {
        Pageable pageableRequest = PageRequest.of(pageNumber, pageSize);
        Query query = new Query();
        query.addCriteria(Criteria.where("sales").gt(0).and("status").ne(StatusEnum.DELETED));

        long totalElement = mongoTemplate.count(query, Product.class);

        query.with(pageableRequest);
        List<Product> products = mongoTemplate.find(query, Product.class);

        long totalPages = totalElement % pageSize == 0 ? totalElement / pageSize : (totalElement / pageSize) + 1;

        return ListProductResponseDTO.builder()
                .products(productMapper.toListProductDTO(products))
                .first(pageNumber == 0)
                .last(pageNumber == totalPages - 1)
                .totalPages(totalPages)
                .totalElements(totalElement)
                .numberOfElements(products.size())
                .build();
    }

    // find all with pagination
    public ListProductResponseDTO findPaginatedProducts(int pageNumber, int pageSize) {
        Pageable pageableRequest = PageRequest.of(pageNumber, pageSize);
        Query query = new Query();
        query.addCriteria(Criteria.where("status").ne(StatusEnum.DELETED));

        long totalElement = mongoTemplate.count(query, Product.class);

        query.with(pageableRequest);
        List<Product> products = mongoTemplate.find(query, Product.class);

        long totalPages = totalElement % pageSize == 0 ? totalElement / pageSize : (totalElement / pageSize) + 1;
        return ListProductResponseDTO.builder()
                .products(productMapper.toListProductDTO(products))
                .first(pageNumber == 0)
                .last(pageNumber == totalPages - 1)
                .totalPages(totalPages)
                .totalElements(totalElement)
                .numberOfElements(products.size())
                .build();
    }

    // find detail
    public ProductDTO findById(String id) throws ProductNotFoundException {
        Product product = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("Product not found", "productNotFound"));
        return productMapper.toProductDTO(product);
    }

    // role job make random 16 sales product
}

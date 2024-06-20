package com.example.apidemo.product.service;

import com.example.apidemo.enums.StatusEnum;
import com.example.apidemo.exception.ProductNotFoundException;
import com.example.apidemo.product.dto.ListProductResponseDTO;
import com.example.apidemo.product.dto.ProductDTO;
import com.example.apidemo.product.entity.Product;
import com.example.apidemo.product.mapper.ProductMapper;
import com.example.apidemo.product.repository.ProductRepository;
import com.example.apidemo.review.dto.ReviewDTO;
import com.example.apidemo.util.UtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProductService {
    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductMapper productMapper;

    @Autowired
    UtilService utilService;

    public ListProductResponseDTO findPaginatedSalesProducts(int pageNumber, int pageSize) {
        Page<Product> products = productRepository.findBySalesAndStatus(StatusEnum.ACTIVE, PageRequest.of(pageNumber, pageSize));

        List<ProductDTO> productDTOS = productMapper.toListProductDTO(products.getContent());

        for(ProductDTO dto : productDTOS) {
            findTotalReviewsAndRatingOfProduct(dto);
        }

        return ListProductResponseDTO.builder()
                .products(productDTOS)
                .first(products.isFirst())
                .last(products.isLast())
                .totalPages(products.getTotalPages())
                .totalElements(products.getTotalElements())
                .numberOfElements(products.getNumberOfElements())
                .build();
    }

    // find all with pagination
    public ListProductResponseDTO findPaginatedProducts(int pageNumber, int pageSize) {
        Page<Product> products = productRepository.findByStatus(StatusEnum.ACTIVE, PageRequest.of(pageNumber, pageSize));

        // long totalPages = totalElement % pageSize == 0 ? totalElement / pageSize : (totalElement / pageSize) + 1;
        List<ProductDTO> productDTOS = productMapper.toListProductDTO(products.getContent());

        for(ProductDTO dto : productDTOS) {
            findTotalReviewsAndRatingOfProduct(dto);
        }

        return ListProductResponseDTO.builder()
                .products(productDTOS)
                .first(products.isFirst())
                .last(products.isLast())
                .totalPages(products.getTotalPages())
                .totalElements(products.getTotalElements())
                .numberOfElements(products.getNumberOfElements())
                .build();
    }

    public ProductDTO findByStatusAndId(String id) throws ProductNotFoundException {
        Product product = productRepository.findByStatusAndId(StatusEnum.ACTIVE, UUID.fromString(id)).orElseThrow(() -> new ProductNotFoundException("Product not found", "productNotFound"));
        ProductDTO productDTO = productMapper.toProductDTO(product);
        findTotalReviewsAndRatingOfProduct(productDTO);
        return  productDTO;
    }

    public void findTotalReviewsAndRatingOfProduct(ProductDTO dto) {
        dto.setTotalReviews(dto.getReviews().size());
        dto.setRating(utilService.calculateAverageRating(dto.getReviews()));
    }
}

package com.example.apidemo.product.dto;

import com.example.apidemo.review.dto.ReviewDTO;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {
    private String id;
    private String name;
    private String description;
    private String category;
    private Integer sales;
    private BigDecimal price;
    private String status;
    private String attachment;
    List<ReviewDTO> reviews;
    private int totalReviews;
    private double rating;
}

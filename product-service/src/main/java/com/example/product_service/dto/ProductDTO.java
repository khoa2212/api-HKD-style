package com.example.product_service.dto;

import lombok.*;

import java.math.BigDecimal;
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
}

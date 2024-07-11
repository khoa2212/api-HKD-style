package com.example.apidemo.wishlist.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class WishlistProductDTO {
    private String id;
    private String name;
    private String description;
    private String category;
    private Integer sales;
    private BigDecimal price;
    private String status;
    private String attachment;
    private Integer stock;
}

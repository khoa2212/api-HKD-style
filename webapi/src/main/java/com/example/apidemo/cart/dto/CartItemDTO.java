package com.example.apidemo.cart.dto;

import com.example.apidemo.product.dto.ProductDTO;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CartItemDTO {
    private String id;
    private ProductDTO product;
    private Integer quantity;
}

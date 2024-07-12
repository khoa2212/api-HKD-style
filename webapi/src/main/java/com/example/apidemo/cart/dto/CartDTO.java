package com.example.apidemo.cart.dto;

import lombok.*;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CartDTO {
    private String userId;
    private Set<CartItemDTO> cartItems;
}

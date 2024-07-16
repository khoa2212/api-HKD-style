package com.example.apidemo.cart.dto;

import lombok.*;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CartDTO {
    private String id;
    private String userId;
    private List<CartItemDTO> cartItems;
}

package com.example.apidemo.wishlist.dto;

import com.example.apidemo.product.dto.ProductDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class WishlistResponseDTO {
    private String userId;
    private List<ProductDTO> products;
}

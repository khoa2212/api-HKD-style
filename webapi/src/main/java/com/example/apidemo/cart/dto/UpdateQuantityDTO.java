package com.example.apidemo.cart.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateQuantityDTO {
    @NotNull
    private String cartId;

    @NotNull
    private String productId;

    @NotNull
    @Min(value = 1, message = "Quantity value must greater than 1")
    private Integer quantity;
}

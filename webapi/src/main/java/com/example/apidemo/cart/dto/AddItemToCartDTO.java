package com.example.apidemo.cart.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AddItemToCartDTO {

    @NotNull
    private String productId;

    @NotNull
    @Min(value = 1, message = "Quantity value must greater than 1")
    private Integer quantity;
}

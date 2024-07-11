package com.example.apidemo.product.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddProductRequestDTO {

    @NotNull
    private String name;

    @NotNull
    private String description;

    @NotNull
    private String category;

    @NotNull
    @Min(value = 0, message = "Price value must greater than 0")
    private BigDecimal price;

    @NotNull
    @Min(value = 0, message = "Stock value must greater than 0")
    @Max(value = 1000, message = "Stock value must smaller than 1000")
    private Integer stock;

    @NotNull
    @Min(value = 0, message = "Sales value must greater than 0")
    @Max(value = 100, message = "Sales value must smaller than 100")
    private Integer sales;

    @NotNull
    private MultipartFile image;
}

package com.example.apidemo.product.dto;

import com.example.apidemo.review.dto.ReviewDTO;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

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
    private BigDecimal price;

    @NotNull
    private Integer stock;

    private Integer sales;

    @NotNull
    private MultipartFile image;
}

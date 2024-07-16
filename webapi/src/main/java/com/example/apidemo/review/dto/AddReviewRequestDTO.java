package com.example.apidemo.review.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddReviewRequestDTO {
    @NotBlank(message = "Product id must not be null or empty")
    private String productId;

    @NotBlank(message = "Name must not be null or empty")
    private String fullName;

    @Min(value = 1, message = "Rating value must greater than 1")
    @Max(value = 5, message = "Rating value must smaller than 5")
    private int rating;

    private String content;
}

package com.example.apidemo.review.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateReviewRequestDTO {
    @JsonProperty("full_name")
    private String fullName;

    private Integer rating;
    private String content;
}

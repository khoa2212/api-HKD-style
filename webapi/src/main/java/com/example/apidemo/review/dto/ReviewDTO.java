package com.example.apidemo.review.dto;

import jakarta.persistence.Column;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewDTO {
    private String id;
    private String userId;
    private int rating;
    private String content;
    private String fullName;

}

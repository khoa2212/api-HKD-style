package com.example.apidemo.review.mapper;

import com.example.apidemo.review.dto.ReviewDTO;
import com.example.apidemo.review.entity.Review;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReviewMapper {
    ReviewDTO toDTO(Review review);

    List<ReviewDTO> toListDTO(List<Review> products);
}

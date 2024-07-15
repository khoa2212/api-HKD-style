package com.example.apidemo.review.service;

import com.example.apidemo.exception.ExceptionMessage;
import com.example.apidemo.exception.ItemNotFoundException;
import com.example.apidemo.product.entity.Product;
import com.example.apidemo.product.repository.ProductRepository;
import com.example.apidemo.review.dto.ReviewDTO;
import com.example.apidemo.review.dto.AddReviewRequestDTO;
import com.example.apidemo.review.dto.UpdateReviewRequestDTO;
import com.example.apidemo.review.entity.Review;
import com.example.apidemo.review.mapper.ReviewMapper;
import com.example.apidemo.review.respository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ReviewService {
    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    ReviewMapper reviewMapper;

    @Autowired
    ProductRepository productRepository;

    public List<ReviewDTO> findAllByProductId(UUID productID) throws ItemNotFoundException {
        Product product = productRepository.findById(productID)
                .orElseThrow(() -> new ItemNotFoundException(ExceptionMessage.PRODUCT_NOT_FOUND, ExceptionMessage.PRODUCT_NOT_FOUND_CODE));
        List<Review> reviews = reviewRepository.findByProductId(productID);;
        return reviewMapper.toListDTO(reviews);
    }

    public ReviewDTO add(UUID userId, AddReviewRequestDTO addReviewRequestDTO) throws ItemNotFoundException {
        Product product = productRepository.findById(UUID.fromString(addReviewRequestDTO.getProductId()))
                .orElseThrow(() -> new ItemNotFoundException(ExceptionMessage.PRODUCT_NOT_FOUND, ExceptionMessage.PRODUCT_NOT_FOUND_CODE));

        Review review = Review.builder().userId(userId)
                .fullName(addReviewRequestDTO.getFullName())
                .content(addReviewRequestDTO.getContent())
                .rating(addReviewRequestDTO.getRating())
                .product(product)
                .build();

        Review addedReview = reviewRepository.save(review);

        return reviewMapper.toDTO(addedReview);
    }

    public ReviewDTO update(String reviewID, UpdateReviewRequestDTO updateReviewRequestDTO) throws ItemNotFoundException {
        Review review = reviewRepository.findById(UUID.fromString(reviewID))
                .orElseThrow(() -> new ItemNotFoundException(ExceptionMessage.REVIEW_NOT_FOUND, ExceptionMessage.PRODUCT_NOT_FOUND_CODE));

        if(updateReviewRequestDTO.getContent() != null) {
            review.setContent(updateReviewRequestDTO.getContent());
        }

        if(updateReviewRequestDTO.getRating() != null) {
            review.setRating(updateReviewRequestDTO.getRating());
        }

        if(!updateReviewRequestDTO.getFullName().equals(review.getFullName())) {
            review.setFullName(updateReviewRequestDTO.getFullName());
        }

        Review updatedReview = reviewRepository.save(review);

        return reviewMapper.toDTO(updatedReview);
    }

    public void delete(String reviewID) throws ItemNotFoundException {
        Review review = reviewRepository.findById(UUID.fromString(reviewID))
                .orElseThrow(() -> new ItemNotFoundException(ExceptionMessage.REVIEW_NOT_FOUND, ExceptionMessage.PRODUCT_NOT_FOUND_CODE));

        reviewRepository.delete(review);
    }
}

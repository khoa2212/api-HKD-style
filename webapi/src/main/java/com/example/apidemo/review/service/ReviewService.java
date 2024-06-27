package com.example.apidemo.review.service;

import com.example.apidemo.exception.ProductNotFoundException;
import com.example.apidemo.exception.ReviewNotFoundException;
import com.example.apidemo.product.entity.Product;
import com.example.apidemo.product.repository.ProductRepository;
import com.example.apidemo.review.dto.ReviewDTO;
import com.example.apidemo.review.dto.AddReviewRequestDTO;
import com.example.apidemo.review.dto.UpdateReviewRequestDTO;
import com.example.apidemo.review.entity.Review;
import com.example.apidemo.review.mapper.ReviewMapper;
import com.example.apidemo.review.respository.ReviewRepository;
import jakarta.validation.Valid;
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
    public List<ReviewDTO> findAllReview() {
        List<Review> listReview = reviewRepository.findAll();
        return reviewMapper.toListDTO(listReview);
    }

    public ReviewDTO findReviewById(UUID reviewID) throws ReviewNotFoundException {
        Review review = reviewRepository.findById(reviewID)
                .orElseThrow(() -> new ReviewNotFoundException("Review not found"));
        return reviewMapper.toDTO(review);
    }

    public List<ReviewDTO> findAllReviewsByProductId(UUID productID, Integer rating) throws ProductNotFoundException {
        Product product = productRepository.findById(productID)
                .orElseThrow(() -> new ProductNotFoundException("Product not found", "NOT_FOUND"));
        List<Review> reviews;
        if (rating == null) {
            reviews = reviewRepository.findByProductId(productID);
        }
        else {
            reviews = reviewRepository.findByProductIdAndRating(productID, rating);
        }
        return reviewMapper.toListDTO(reviews);
    }

    public ReviewDTO addReview(AddReviewRequestDTO addReviewRequestDTO) throws ProductNotFoundException {
        Product product = productRepository.findById(UUID.fromString(addReviewRequestDTO.getProductId()))
                .orElseThrow(() -> new ProductNotFoundException("Product not found", "NOT_FOUND"));

        Review review = Review.builder().userId(UUID.fromString(addReviewRequestDTO.getUserId()))
                .fullName(addReviewRequestDTO.getFullName())
                .content(addReviewRequestDTO.getContent())
                .rating(addReviewRequestDTO.getRating())
                .product(product)
                .build();

        Review addedReview = reviewRepository.save(review);

        return reviewMapper.toDTO(addedReview);
    }

    public ReviewDTO updateReview(@Valid String reviewID, UpdateReviewRequestDTO updateReviewRequestDTO) throws ReviewNotFoundException {
        Review review = reviewRepository.findById(UUID.fromString(reviewID))
                .orElseThrow(() -> new ReviewNotFoundException("Not found review"));

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

    public void deleteReview(@Valid String reviewID) throws ReviewNotFoundException {
        Review review = reviewRepository.findById(UUID.fromString(reviewID))
                .orElseThrow(() -> new ReviewNotFoundException("Review not found"));

        reviewRepository.delete(review);
    }
}

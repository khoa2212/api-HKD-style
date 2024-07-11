package com.example.apidemo.review.respository;

import com.example.apidemo.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, UUID> {

    List<Review> findByProductId(UUID productID);
    List<Review> findByProductIdAndRating(UUID productID, Integer rating);
}

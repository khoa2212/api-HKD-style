package com.example.apidemo.review.controller;

import com.example.apidemo.exception.ExceptionMessage;
import com.example.apidemo.exception.ItemNotFoundException;
import com.example.apidemo.body.BodyContent;
import com.example.apidemo.review.dto.ReviewDTO;
import com.example.apidemo.review.dto.AddReviewRequestDTO;
import com.example.apidemo.review.dto.UpdateReviewRequestDTO;
import com.example.apidemo.review.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
public class ReviewController {
    @Autowired
    ReviewService reviewService;

    @GetMapping(path = "/products/{productId}/reviews")
    public ResponseEntity<List<ReviewDTO>> findAllByProductId(
            @PathVariable("productId") String productId)
            throws ItemNotFoundException {
        return ResponseEntity.ok(reviewService.findAllByProductId(UUID.fromString(productId)));
    }
    @PostMapping(path = "/reviews/user/{userId}")
    public ResponseEntity<BodyContent<ReviewDTO>> add(@PathVariable("userId") String userId, @Valid @RequestBody AddReviewRequestDTO addReviewRequestDTO) throws ItemNotFoundException {
        ReviewDTO addedReviewDTO = reviewService.add(userId, addReviewRequestDTO);
        return ResponseEntity.created(URI.create("/reviews/" + addedReviewDTO.getId()))
                .body(new BodyContent<>(HttpStatus.CREATED.value(),
                        ExceptionMessage.SUCCESS_MESSAGE,
                        addedReviewDTO));
    }
    @PutMapping(path = "/reviews/{reviewId}")
    public ResponseEntity<BodyContent<ReviewDTO>> update(@PathVariable("reviewId") String reviewId, @Valid @RequestBody UpdateReviewRequestDTO updateReviewRequestDTO) throws ItemNotFoundException {
        ReviewDTO updatedReviewDTO = reviewService.update(reviewId, updateReviewRequestDTO);
        return ResponseEntity.ok()
                .body(new BodyContent<>(HttpStatus.OK.value(),
                        ExceptionMessage.SUCCESS_MESSAGE,
                        updatedReviewDTO));
    }

    @DeleteMapping(path = "/reviews/{reviewId}")
    public ResponseEntity<Void> delete(@PathVariable("reviewId") String reviewId) throws ItemNotFoundException {
        reviewService.delete(reviewId);
        return ResponseEntity.noContent().build();
    }
}

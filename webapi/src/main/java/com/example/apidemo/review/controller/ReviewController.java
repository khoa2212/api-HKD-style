package com.example.apidemo.review.controller;

import com.example.apidemo.exception.ExceptionMessage;
import com.example.apidemo.exception.ProductNotFoundException;
import com.example.apidemo.exception.ReviewNotFoundException;
import com.example.apidemo.body.BodyContent;
import com.example.apidemo.review.dto.ReviewDTO;
import com.example.apidemo.review.dto.AddReviewRequestDTO;
import com.example.apidemo.review.dto.UpdateReviewRequestDTO;
import com.example.apidemo.review.service.ReviewService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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

    @GetMapping(path = "/reviews")
    public ResponseEntity<List<ReviewDTO>> findAll() {

        return ResponseEntity.ok(reviewService.findAll());
    }

    @GetMapping(path = "/reviews/{reviewID}")
    public ResponseEntity<ReviewDTO> findById(@PathVariable("reviewID") UUID reviewID) throws ReviewNotFoundException {
        return ResponseEntity.ok(reviewService.findById(reviewID));
    }

    @GetMapping(path = "/products/{productID}/reviews")
    public ResponseEntity<List<ReviewDTO>> findAllByProductId(
            @PathVariable("productID") UUID productID,
            @RequestParam(value = "rating", required = false) @Min(1) @Max(5) Integer rating)
            throws ProductNotFoundException {
        return ResponseEntity.ok(reviewService.findAllByProductId(productID, rating));
    }
    @PostMapping(path = "/reviews")
    public ResponseEntity<BodyContent<ReviewDTO>> add(@Valid @RequestBody AddReviewRequestDTO addReviewRequestDTO) throws ProductNotFoundException {
        ReviewDTO addedReviewDTO = reviewService.add(addReviewRequestDTO);
        return ResponseEntity.created(URI.create("/reviews/" + addedReviewDTO.getId()))
                .body(new BodyContent<>(HttpStatus.CREATED.value(),
                        ExceptionMessage.SUCCESS_MESSAGE,
                        addedReviewDTO));
    }
    @PutMapping(path = "/reviews/{reviewID}")
    public ResponseEntity<BodyContent<ReviewDTO>> update(@PathVariable String reviewID, @Valid @RequestBody UpdateReviewRequestDTO updateReviewRequestDTO) throws ReviewNotFoundException {
        ReviewDTO updatedReviewDTO = reviewService.update(reviewID, updateReviewRequestDTO);
        return ResponseEntity.ok()
                .body(new BodyContent<>(HttpStatus.OK.value(),
                        ExceptionMessage.SUCCESS_MESSAGE,
                        updatedReviewDTO));
    }

    @DeleteMapping(path = "/reviews/{reviewID}")
    public ResponseEntity<Void> delete(@PathVariable String reviewID) throws ReviewNotFoundException {
        reviewService.delete(reviewID);
        return ResponseEntity.noContent().build();
    }
}

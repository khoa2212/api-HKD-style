package com.example.apidemo.review.controller;

import com.example.apidemo.exception.ProductNotFoundException;
import com.example.apidemo.exception.ReviewNotFoundException;
import com.example.apidemo.payload.Payload;
import com.example.apidemo.review.dto.ReviewDTO;
import com.example.apidemo.review.dto.AddReviewRequestDTO;
import com.example.apidemo.review.dto.UpdateReviewRequestDTO;
import com.example.apidemo.review.service.ReviewService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class ReviewController {
    @Autowired
    ReviewService reviewService;

    @GetMapping(path = "/reviews")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Payload<List<ReviewDTO>>> findAllReview() {

        return ResponseEntity.ok(new Payload<>(HttpStatus.OK.value(),
                "SUCCESS",
                reviewService.findAllReview()));
    }

    @GetMapping(path = "/reviews/{reviewID}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Payload<ReviewDTO>> findReviewById(@PathVariable("reviewID") UUID reviewID) throws ReviewNotFoundException {
        return ResponseEntity.ok(new Payload<>(HttpStatus.OK.value(),
                "SUCCESS",
                reviewService.findReviewById(reviewID)));
    }

    @GetMapping(path = "/products/{productID}/reviews")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Payload<List<ReviewDTO>>> findAllReviewsByProductId(
            @PathVariable("productID") UUID productID,
            @RequestParam(value = "rating", required = false) @Min(1) @Max(5) Integer rating)
            throws ProductNotFoundException {
        return ResponseEntity.ok(new Payload<>(HttpStatus.OK.value(), "SUCCESS", reviewService.findAllReviewsByProductId(productID, rating)));
    }
    @PostMapping(path = "/reviews")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Payload<ReviewDTO>> addReview(@Valid @RequestBody AddReviewRequestDTO addReviewRequestDTO) throws ProductNotFoundException {
        System.out.println(addReviewRequestDTO.getFullName());
        System.out.println(addReviewRequestDTO.getContent());
        ReviewDTO addedReviewDTO = reviewService.addReview(addReviewRequestDTO);
        return ResponseEntity.created(URI.create("/reviews/" + addedReviewDTO.getId()))
                .body(new Payload<>(HttpStatus.CREATED.value(),
                        "SUCCESS",
                        addedReviewDTO));
    }
    @PutMapping(path = "reviews/{reviewID}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<Payload<ReviewDTO>> updateReview(@PathVariable String reviewID, @Valid @RequestBody UpdateReviewRequestDTO updateReviewRequestDTO) throws ReviewNotFoundException {
        ReviewDTO updatedReviewDTO = reviewService.updateReview(reviewID, updateReviewRequestDTO);
        return ResponseEntity.accepted()
                .body(new Payload<>(HttpStatus.ACCEPTED.value(),
                        "SUCCESS",
                        updatedReviewDTO));
    }

    @DeleteMapping(path = "reviews/{reviewID}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Null> deleteReview(@PathVariable String reviewID) throws ReviewNotFoundException {
        reviewService.deleteReview(reviewID);
        return ResponseEntity.noContent().build();
    }
}

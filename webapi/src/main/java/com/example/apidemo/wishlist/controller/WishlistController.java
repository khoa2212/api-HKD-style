package com.example.apidemo.wishlist.controller;

import com.example.apidemo.exception.ProductNotFoundException;
import com.example.apidemo.wishlist.dto.AddProductToWishlistDTO;
import com.example.apidemo.wishlist.dto.WishlistProductDTO;
import com.example.apidemo.wishlist.dto.WishlistResponseDTO;
import com.example.apidemo.wishlist.service.WishlistService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class WishlistController {
    private final WishlistService wishlistService;
    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @GetMapping(
            path = "/wishlists/{userId}",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<WishlistResponseDTO> getWishListByUserId(
        @PathVariable("userId") String userId
    ) {
        return ResponseEntity.ok(wishlistService.getWishListByUser(UUID.fromString(userId)));
    }

    @PostMapping(
            path = "/wishlists/{userId}",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<WishlistProductDTO> addProductToWishlist(
            @PathVariable("userId") String userId,
            @RequestBody AddProductToWishlistDTO request
    ) throws ProductNotFoundException {
        return ResponseEntity.ok(wishlistService.addProductToWishlist(
                UUID.fromString(userId), UUID.fromString(request.getProductId())));
    }
}

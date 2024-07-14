package com.example.apidemo.wishlist.controller;

import com.example.apidemo.exception.ItemNotFoundException;
import com.example.apidemo.product.dto.ProductDTO;
import com.example.apidemo.wishlist.dto.ChangeProductInWishlistDTO;
import com.example.apidemo.wishlist.dto.WishlistResponseDTO;
import com.example.apidemo.wishlist.service.WishlistService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class WishlistController {

    @Autowired
    WishlistService wishlistService;

    @GetMapping(path = "/wishlists/users/{userId}")
    public ResponseEntity<WishlistResponseDTO> getByUserId(
        @PathVariable("userId") String userId
    ) {
        return ResponseEntity.ok(wishlistService.getByUserId(UUID.fromString(userId)));
    }

    @PostMapping(path = "/wishlists/users/{userId}")
    public ResponseEntity<ProductDTO> addProductToWishlist(
            @PathVariable("userId") String userId,
            @Valid @RequestBody ChangeProductInWishlistDTO request
    ) throws ItemNotFoundException {
        return ResponseEntity.ok(wishlistService.addProductToWishlist(
                UUID.fromString(userId), UUID.fromString(request.getProductId())));
    }

    @DeleteMapping(path = "/wishlists/{itemId}")
    public ResponseEntity<Void> removeProductFromWishlist(
            @PathVariable("itemId") String itemId
    ) throws ItemNotFoundException {
        wishlistService.removeProductFromWishlist(UUID.fromString(itemId));
        return ResponseEntity.noContent().build();
    }
}

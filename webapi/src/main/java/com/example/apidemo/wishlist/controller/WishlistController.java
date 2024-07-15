package com.example.apidemo.wishlist.controller;

import com.example.apidemo.exception.BadRequestException;
import com.example.apidemo.exception.ItemNotFoundException;
import com.example.apidemo.product.dto.ProductDTO;
import com.example.apidemo.util.User;
import com.example.apidemo.util.UtilService;
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

    @Autowired
    UtilService utilService;

    @GetMapping(path = "/wishlists")
    public ResponseEntity<WishlistResponseDTO> getByUserId(
    ) {
        User authUser = utilService.getUserFromContext();
        return ResponseEntity.ok(wishlistService.getByUserId(authUser.getId()));
    }

    @PostMapping(path = "/wishlists")
    public ResponseEntity<ProductDTO> addProductToWishlist(
            @Valid @RequestBody ChangeProductInWishlistDTO request
    ) throws ItemNotFoundException, BadRequestException {
        User authUser = utilService.getUserFromContext();
        return ResponseEntity.ok(wishlistService.addProductToWishlist(
                authUser.getId(), UUID.fromString(request.getProductId())));
    }

    @DeleteMapping(path = "/wishlists/{itemId}")
    public ResponseEntity<Void> removeProductFromWishlist(
            @PathVariable("itemId") String itemId
    ) throws ItemNotFoundException {
        wishlistService.removeProductFromWishlist(UUID.fromString(itemId));
        return ResponseEntity.noContent().build();
    }
}

package com.example.apidemo.cart.controller;

import com.example.apidemo.cart.dto.AddItemToCartDTO;
import com.example.apidemo.cart.dto.CartDTO;
import com.example.apidemo.cart.service.CartService;
import com.example.apidemo.exception.ProductNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class CartController {
    @Autowired
    CartService cartService;

    @GetMapping(path = "carts/users/{userId}")
    public ResponseEntity<CartDTO> findByUserId(@PathVariable("userId") String userId) {
        return ResponseEntity.ok(cartService.findByUserId(UUID.fromString(userId)));
    }

    @PostMapping(path = "carts/users/{userId}")
    public ResponseEntity<CartDTO> add(@PathVariable("userId") String userId, @Valid @RequestBody AddItemToCartDTO addItemToCartDTO) throws ProductNotFoundException {
        CartDTO cartDTO = cartService.addItemToCart(UUID.fromString(userId), addItemToCartDTO);
        return ResponseEntity.ok(cartDTO);
    }
}

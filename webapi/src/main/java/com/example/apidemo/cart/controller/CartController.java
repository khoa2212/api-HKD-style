package com.example.apidemo.cart.controller;

import com.example.apidemo.cart.dto.AddItemToCartDTO;
import com.example.apidemo.cart.dto.CartDTO;
import com.example.apidemo.cart.dto.UpdateQuantityDTO;
import com.example.apidemo.cart.service.CartService;
import com.example.apidemo.exception.BadRequestException;
import com.example.apidemo.exception.ItemNotFoundException;
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
    public ResponseEntity<CartDTO> addItemToCart(@PathVariable("userId") String userId, @Valid @RequestBody AddItemToCartDTO addItemToCartDTO) throws ItemNotFoundException, BadRequestException {
        CartDTO cartDTO = cartService.addItemToCart(UUID.fromString(userId), addItemToCartDTO);
        return ResponseEntity.ok(cartDTO);
    }

    @PutMapping("carts/update-quantity")
    public ResponseEntity<CartDTO> updateQuantity(@Valid @RequestBody UpdateQuantityDTO updateQuantityDTO) throws BadRequestException, ItemNotFoundException {
        CartDTO cartDTO = cartService.updateQuantity(updateQuantityDTO);
        return ResponseEntity.ok(cartDTO);
    }

    @DeleteMapping("carts/items/{itemId}")
    public ResponseEntity<Void> removeItemFromCart(@PathVariable("itemId") String itemId) throws ItemNotFoundException {
        cartService.removeItemFromCart(UUID.fromString(itemId));
        return ResponseEntity.noContent().build();
    }
}

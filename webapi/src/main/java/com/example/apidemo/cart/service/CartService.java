package com.example.apidemo.cart.service;

import com.example.apidemo.cart.dto.AddItemToCartDTO;
import com.example.apidemo.cart.dto.CartDTO;
import com.example.apidemo.cart.entity.Cart;
import com.example.apidemo.cart.entity.CartItem;
import com.example.apidemo.cart.mapper.CartMapper;
import com.example.apidemo.cart.repository.CartItemRepository;
import com.example.apidemo.cart.repository.CartRepository;
import com.example.apidemo.exception.ExceptionMessage;
import com.example.apidemo.exception.ProductNotFoundException;
import com.example.apidemo.product.entity.Product;
import com.example.apidemo.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class CartService {
    @Autowired
    CartRepository cartRepository;

    @Autowired
    CartMapper cartMapper;

    @Autowired
    CartItemRepository cartItemRepository;

    @Autowired
    ProductRepository productRepository;

    public CartDTO findByUserId(UUID userId) {
        Optional<Cart> cart = cartRepository.findByUserId(userId);
        return cart.map(value -> cartMapper.toDTO(value)).orElse(null);
    }

    public CartDTO addItemToCart(UUID userId, AddItemToCartDTO addItemToCartDTO) throws ProductNotFoundException {
        Optional<Cart> cart = cartRepository.findByUserId(userId);
        Product product = productRepository.findById(UUID.fromString(addItemToCartDTO.getProductId())).orElseThrow(() -> new ProductNotFoundException(ExceptionMessage.PRODUCT_NOT_FOUND, ExceptionMessage.REVIEW_NOT_FOUND_CODE));
        if(cart.isPresent()) {
            Cart myCart = cart.get();
            CartItem cartItem = CartItem
                    .builder()
                    .cart(myCart)
                    .product(product)
                    .quantity(addItemToCartDTO.getQuantity())
                    .build();

            cartItemRepository.save(cartItem);
        }
        else {
            Cart newCart = Cart
                    .builder()
                    .userId(userId)
                    .build();

            cartRepository.save(newCart);

            CartItem cartItem = CartItem
                    .builder()
                    .cart(newCart)
                    .product(product)
                    .quantity(addItemToCartDTO.getQuantity())
                    .build();

            cartItemRepository.save(cartItem);
        }

        CartDTO cartDTO = findByUserId(userId);

        return cartDTO;
    }
}

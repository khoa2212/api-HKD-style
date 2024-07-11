package com.example.apidemo.cart.service;

import com.example.apidemo.cart.repository.CartItemRepository;
import com.example.apidemo.cart.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartService {
    @Autowired
    CartRepository cartRepository;

    @Autowired
    CartItemRepository cartItemRepository;
}

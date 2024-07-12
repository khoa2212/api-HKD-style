package com.example.apidemo.cart.controller;

import com.example.apidemo.cart.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CartController {
    @Autowired
    CartService cartService;
}

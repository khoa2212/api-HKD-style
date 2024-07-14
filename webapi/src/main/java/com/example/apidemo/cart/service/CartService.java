package com.example.apidemo.cart.service;

import com.example.apidemo.cart.dto.AddItemToCartDTO;
import com.example.apidemo.cart.dto.CartDTO;
import com.example.apidemo.cart.dto.UpdateQuantityDTO;
import com.example.apidemo.cart.entity.Cart;
import com.example.apidemo.cart.entity.CartItem;
import com.example.apidemo.cart.mapper.CartMapper;
import com.example.apidemo.cart.repository.CartItemRepository;
import com.example.apidemo.cart.repository.CartRepository;
import com.example.apidemo.exception.BadRequestException;
import com.example.apidemo.exception.ExceptionMessage;
import com.example.apidemo.exception.ItemNotFoundException;
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

    public CartDTO addItemToCart(UUID userId, AddItemToCartDTO addItemToCartDTO) throws ItemNotFoundException, BadRequestException {
        Optional<Cart> cart = cartRepository.findByUserId(userId);
        Product product = productRepository.findById(UUID.fromString(addItemToCartDTO.getProductId())).orElseThrow(() -> new ItemNotFoundException(ExceptionMessage.PRODUCT_NOT_FOUND, ExceptionMessage.REVIEW_NOT_FOUND_CODE));

        checkQuantity(addItemToCartDTO.getQuantity(), product.getStock());

        if(cart.isPresent()) {
            Cart myCart = cart.get();
            Optional<CartItem> existCartItem = cartItemRepository.
                    findByCartIdAndProductId(myCart.getId(), product.getId());

            if(existCartItem.isPresent()) {
                throw new BadRequestException(ExceptionMessage.ITEM_ALREADY_EXIST_IN_CART, ExceptionMessage.ITEM_ALREADY_EXIST_IN_CART_CODE);
            }

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

        return findByUserId(userId);
    }

    public void checkQuantity(int quantity, int stock) throws BadRequestException {
        if(quantity > stock) {
            throw new BadRequestException(ExceptionMessage.OVER_QUANTITY, ExceptionMessage.OVER_QUANTITY_CODE);
        }
    }

    public CartDTO updateQuantity(UpdateQuantityDTO updateQuantityDTO) throws ItemNotFoundException, BadRequestException {
        Cart cart = cartRepository.findById(UUID.fromString(updateQuantityDTO.getCartId())).orElseThrow(() -> new ItemNotFoundException(ExceptionMessage.CART_NOT_FOUND, ExceptionMessage.CART_NOT_FOUND_CODE));
        Product product = productRepository.findById(UUID.fromString(updateQuantityDTO.getProductId())).orElseThrow(() -> new ItemNotFoundException(ExceptionMessage.PRODUCT_NOT_FOUND, ExceptionMessage.REVIEW_NOT_FOUND_CODE));

        checkQuantity(updateQuantityDTO.getQuantity(), product.getStock());

        CartItem cartItem = cartItemRepository.
                findByCartIdAndProductId(cart.getId(), product.getId()).orElseThrow(() -> new ItemNotFoundException(ExceptionMessage.CART_ITEM_NOT_FOUND, ExceptionMessage.CART_ITEM_NOT_FOUND_CODE));

        cartItem.setQuantity(updateQuantityDTO.getQuantity());

        cartItemRepository.save(cartItem);

        CartDTO myCart = cartMapper.toDTO(cartRepository.findById(cart.getId()).get());

        return myCart;
    }
}

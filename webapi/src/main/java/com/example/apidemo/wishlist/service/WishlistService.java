package com.example.apidemo.wishlist.service;

import com.example.apidemo.exception.ExceptionMessage;
import com.example.apidemo.exception.ProductNotFoundException;
import com.example.apidemo.product.entity.Product;
import com.example.apidemo.product.repository.ProductRepository;
import com.example.apidemo.wishlist.dto.WishlistProductDTO;
import com.example.apidemo.wishlist.dto.WishlistResponseDTO;
import com.example.apidemo.wishlist.entity.Wishlist;
import com.example.apidemo.wishlist.mapper.WishlistMapper;
import com.example.apidemo.wishlist.repository.WishlistRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class WishlistService {
    private final WishlistRepository wishlistRepository;
    private final ProductRepository productRepository;
    private final WishlistMapper wishlistMapper;

    public WishlistService(
            WishlistRepository wishlistRepository,
            ProductRepository productRepository,
            WishlistMapper wishlistMapper) {
        this.wishlistRepository = wishlistRepository;
        this.productRepository = productRepository;
        this.wishlistMapper = wishlistMapper;
    }

    public List<WishlistProductDTO> getWishlistProductByUserId(UUID userId) {
        return wishlistRepository.findAllByUserId(userId).stream()
                .map(wishlist -> wishlistMapper.toWishListProductDTO(wishlist.getProduct()))
                .toList();
    }

    public WishlistResponseDTO getByUserId(UUID userId) {
        List<WishlistProductDTO> products = getWishlistProductByUserId(userId);

        return WishlistResponseDTO.builder()
                .userId(userId.toString())
                .products(products)
                .build();
    }

    public WishlistProductDTO addProductToWishlist(UUID userId, UUID productId) throws ProductNotFoundException {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ProductNotFoundException(ExceptionMessage.PRODUCT_NOT_FOUND, ExceptionMessage.PRODUCT_NOT_FOUND_CODE));
        Wishlist wishlist = Wishlist.builder()
                .userId(userId)
                .product(product)
                .build();

        wishlistRepository.save(wishlist);
        return wishlistMapper.toWishListProductDTO(product);
    }

    public void removeProductFromWishlist(UUID itemId) throws ProductNotFoundException {
        Wishlist item = wishlistRepository.findById(itemId).orElseThrow(
                () -> new ProductNotFoundException(ExceptionMessage.PRODUCT_NOT_FOUND, ExceptionMessage.PRODUCT_NOT_FOUND_CODE));

        wishlistRepository.delete(item);
    }
}

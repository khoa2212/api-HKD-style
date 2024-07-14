package com.example.apidemo.wishlist.service;

import com.example.apidemo.exception.ExceptionMessage;
import com.example.apidemo.exception.ItemNotFoundException;
import com.example.apidemo.product.dto.ProductDTO;
import com.example.apidemo.product.entity.Product;
import com.example.apidemo.product.repository.ProductRepository;
import com.example.apidemo.util.UtilService;
import com.example.apidemo.wishlist.dto.WishlistResponseDTO;
import com.example.apidemo.wishlist.entity.Wishlist;
import com.example.apidemo.wishlist.mapper.WishlistMapper;
import com.example.apidemo.wishlist.repository.WishlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class WishlistService {

    @Autowired
    WishlistRepository wishlistRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    WishlistMapper wishlistMapper;

    @Autowired
    UtilService utilService;

    public List<ProductDTO> getWishlistProductByUserId(UUID userId) {
        List<ProductDTO> ls = wishlistRepository.findAllByUserId(userId).stream()
                .map(wishlist -> wishlistMapper.toWishListProductDTO(wishlist.getProduct()))
                .toList();

        ls.forEach(item -> {
            item.setTotalReviews(item.getReviews().size());
            item.setRating(utilService.calculateAverageRating(item.getReviews()));
        });

        return ls;
    }

    public WishlistResponseDTO getByUserId(UUID userId) {
        List<ProductDTO> products = getWishlistProductByUserId(userId);

        return WishlistResponseDTO.builder()
                .userId(userId.toString())
                .products(products)
                .build();
    }

    public ProductDTO addProductToWishlist(UUID userId, UUID productId) throws ItemNotFoundException {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ItemNotFoundException(ExceptionMessage.PRODUCT_NOT_FOUND, ExceptionMessage.PRODUCT_NOT_FOUND_CODE));
        Wishlist wishlist = Wishlist.builder()
                .userId(userId)
                .product(product)
                .build();

        wishlistRepository.save(wishlist);
        return wishlistMapper.toWishListProductDTO(product);
    }

    public void removeProductFromWishlist(UUID itemId) throws ItemNotFoundException {
        Wishlist item = wishlistRepository.findById(itemId).orElseThrow(
                () -> new ItemNotFoundException(ExceptionMessage.PRODUCT_NOT_FOUND, ExceptionMessage.PRODUCT_NOT_FOUND_CODE));

        wishlistRepository.delete(item);
    }
}

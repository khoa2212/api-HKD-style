package com.example.apidemo.wishlist.mapper;

import com.example.apidemo.product.entity.Product;
import com.example.apidemo.wishlist.dto.WishlistProductDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WishlistMapper {
    public WishlistProductDTO toWishListProductDTO(Product product);

}

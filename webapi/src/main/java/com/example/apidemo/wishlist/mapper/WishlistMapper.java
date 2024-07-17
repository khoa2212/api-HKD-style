package com.example.apidemo.wishlist.mapper;

import com.example.apidemo.product.dto.ProductDTO;
import com.example.apidemo.product.entity.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WishlistMapper {
    public ProductDTO toWishListProductDTO(Product product);

}

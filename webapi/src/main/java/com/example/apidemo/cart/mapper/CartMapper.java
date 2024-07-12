package com.example.apidemo.cart.mapper;

import com.example.apidemo.cart.dto.CartDTO;
import com.example.apidemo.cart.entity.Cart;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CartMapper {
    public CartDTO toDTO(Cart cart);
    public List<CartDTO> toList(List<Cart> carts);
}

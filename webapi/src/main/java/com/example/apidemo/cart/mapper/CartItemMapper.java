package com.example.apidemo.cart.mapper;

import com.example.apidemo.cart.dto.CartItemDTO;
import com.example.apidemo.cart.entity.CartItem;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CartItemMapper {
    public CartItemDTO toDTO(CartItem cart);
    public List<CartItemDTO> toList(List<CartItem> carts);
}

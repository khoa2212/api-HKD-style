package com.example.product_service.mapper;

import com.example.product_service.dto.ProductDTO;
import com.example.product_service.entity.Product;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductDTO toProductDTO(Product product);

    List<ProductDTO> toListProductDTO(List<Product> products);
}

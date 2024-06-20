package com.example.apidemo.product.mapper;

import com.example.apidemo.product.dto.ProductDTO;
import com.example.apidemo.product.entity.Product;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductDTO toProductDTO(Product product);

    List<ProductDTO> toListProductDTO(List<Product> products);
}

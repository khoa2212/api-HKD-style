package com.example.apidemo.product.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ListProductResponseDTO {
    private List<ProductDTO> products;
    private long totalPages;
    private long totalElements;
    private long numberOfElements;
    private boolean last;
    private boolean first;
}

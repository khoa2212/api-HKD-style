package com.example.apidemo.product.controller;

import com.example.apidemo.body.BodyContent;
import com.example.apidemo.exception.ExceptionMessage;
import com.example.apidemo.exception.ProductNotFoundException;
import com.example.apidemo.product.dto.AddProductRequestDTO;
import com.example.apidemo.product.dto.ListProductResponseDTO;
import com.example.apidemo.product.dto.ProductDTO;
import com.example.apidemo.product.dto.UpdateProductRequestDTO;
import com.example.apidemo.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;

@RestController
public class ProductController {

    @Autowired
    ProductService productService;

    @Operation(summary = "Get a list of products")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return a product list successfully",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ListProductResponseDTO.class))}
            ),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping(path = "/products")
    public ResponseEntity<ListProductResponseDTO> findPaginatedProducts(@RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        return ResponseEntity.ok().body(productService.findPaginatedProducts(pageNumber, pageSize));
    }

    @GetMapping(path = "/products/{id}")
    public ResponseEntity<ProductDTO> findByStatusAndId(@PathVariable("id") String id) throws ProductNotFoundException {
        return ResponseEntity.ok().body(productService.findByStatusAndId(id));
    }

    @GetMapping(path = "/products/sales")
    public ResponseEntity<ListProductResponseDTO> findPaginatedSalesProducts(@RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        return ResponseEntity.ok().body(productService.findPaginatedSalesProducts(pageNumber, pageSize));
    }

    @PostMapping(path = "/products")
    public ResponseEntity<BodyContent<ProductDTO>> add(@Valid @ModelAttribute AddProductRequestDTO addProductRequestDTO) throws IOException {
        ProductDTO productDTO = productService.add(addProductRequestDTO);
        return ResponseEntity.created(URI.create("/products/" + productDTO.getId()))
                .body(new BodyContent<>(HttpStatus.CREATED.value(), ExceptionMessage.SUCCESS_MESSAGE, productDTO));
    }

    @DeleteMapping(path = "/products/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") String id) throws ProductNotFoundException {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(path = "/products/{id}")
    public ResponseEntity<BodyContent<ProductDTO>> update(@PathVariable("id") String id, @Valid @ModelAttribute UpdateProductRequestDTO updateProductRequestDTO) throws IOException, ProductNotFoundException {
        ProductDTO productDTO = productService.update(id, updateProductRequestDTO);
        return ResponseEntity.ok().body(new BodyContent<>(HttpStatus.OK.value(), ExceptionMessage.SUCCESS_MESSAGE, productDTO));
    }
}


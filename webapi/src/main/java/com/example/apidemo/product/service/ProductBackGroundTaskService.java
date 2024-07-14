package com.example.apidemo.product.service;

import com.example.apidemo.exception.ItemNotFoundException;
import com.example.apidemo.product.entity.Product;
import com.example.apidemo.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.*;

@Configuration
public class ProductBackGroundTaskService {

    @Autowired
    ProductRepository productRepository;

    public void runTask() throws ItemNotFoundException {
        System.out.println("Bat dau thoi");

        List<Product> products = productRepository.findAll();
        int numberOfSaleProduct = 16;
        int size = products.size();

        Set<String> saleProductIds = new HashSet<>();
        Set<String> notSaleProductIds = new HashSet<>();
        Random random = new Random();

        while (saleProductIds.size() < numberOfSaleProduct) {
            Product product = products.get(random.nextInt(size));
            saleProductIds.add(product.getId().toString());
        }

        while (notSaleProductIds.size() < (size - numberOfSaleProduct)) {
            Product product = products.get(random.nextInt(size));
            if(!saleProductIds.contains(product.getId().toString())) {
                notSaleProductIds.add(product.getId().toString());
            }
        }

        for(String id : saleProductIds) {
            Product product = productRepository.findById(UUID.fromString(id)).orElseThrow(() -> new ItemNotFoundException("Product not found", "productNotFound"));
            product.setSales(random.nextInt(100 - 20 + 1) + 20);
            productRepository.save(product);
        }

        for(String id : notSaleProductIds) {
            Product product = productRepository.findById(UUID.fromString(id)).orElseThrow(() -> new ItemNotFoundException("Product not found", "productNotFound"));
            product.setSales(0);
            productRepository.save(product);
        }

        System.out.println("Sales thoi");
    }
}
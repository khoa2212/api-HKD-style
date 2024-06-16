package com.example.product_service.service;

import com.example.product_service.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

@Configuration
public class ProductBackGroundTask {

    @Autowired
    MongoTemplate mongoTemplate;

    public void runTask() {
        List<Product> products = mongoTemplate.findAll(Product.class);
        int numberOfSaleProduct = 1;
        int size = products.size();

        Set<String> saleProductIds = new HashSet<>();
        Set<String> notSaleProductIds = new HashSet<>();
        Random random = new Random();

        while (saleProductIds.size() < numberOfSaleProduct) {
            Product product = products.get(random.nextInt(size));
            saleProductIds.add(product.getId());
        }

        while (notSaleProductIds.size() < (size - numberOfSaleProduct)) {
            Product product = products.get(random.nextInt(size));
            if(!saleProductIds.contains(product.getId())) {
                notSaleProductIds.add(product.getId());
            }
        }

        for(String id : saleProductIds) {
            Query query = new Query(Criteria.where("id").is(id));
            Update update = new Update();
            update.set("sales", random.nextInt(100 - 20 + 1) + 20);
            mongoTemplate.updateFirst(query, update, Product.class);
        }

        for(String id : notSaleProductIds) {
            Query query = new Query(Criteria.where("id").is(id));
            Update update = new Update();
            update.set("sales", 0);
            mongoTemplate.updateFirst(query, update, Product.class);
        }

        System.out.println("Sales thoi");
    }
}

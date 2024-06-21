package com.example.apidemo.util;

import com.example.apidemo.exception.ProductNotFoundException;
import com.example.apidemo.product.service.ProductBackGroundTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@EnableScheduling
@Configuration
public class BackGroundTask {

    @Autowired
    ProductBackGroundTaskService productBackGroundTaskService;

    @Scheduled(cron = "0 30 5 * * *") // run at 5:30 AM
    public void randomSalesProduct() throws ProductNotFoundException {
        System.out.println("Scheduled");
        productBackGroundTaskService.runTask();
    }
}
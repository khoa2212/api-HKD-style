package com.example.product_service;

import com.example.product_service.service.ProductBackGroundTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@EnableScheduling
@Configuration
public class BackGroundTask {

    @Autowired
    ProductBackGroundTask productBackGroundTask;

    @Scheduled(cron = "0 34 0 * * *")
    public void randomSalesProduct() {
        productBackGroundTask.runTask();
    }
}

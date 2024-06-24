package com.example.apidemo.cloudinary;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {

    private static final String CLOUD_NAME = "dtzgqc50b";
    private static final String API_KEY = "289717381311262";
    private static final String API_SECRET = "4jvXY7ObtzjC44KV_7Rj3vR--Eg";

    @Bean
    public Cloudinary cloudinary(){
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name",CLOUD_NAME);
        config.put("api_key",API_KEY);
        config.put("api_secret",API_SECRET);

        return new Cloudinary(config);
    }
}

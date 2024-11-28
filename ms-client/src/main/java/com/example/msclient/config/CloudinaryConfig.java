package com.example.msclient.config;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {
    @Bean
    public Cloudinary cloudinary() {
        final Map<String, String> config = new HashMap<>();
        config.put("cloud_name", "dcolq5yxn");
        config.put("api_key", "166251227999812");
        config.put("api_secret", "KOg-kNM26REHg0IEmcOAXt73lvE");
        return new Cloudinary(config);
    }

}

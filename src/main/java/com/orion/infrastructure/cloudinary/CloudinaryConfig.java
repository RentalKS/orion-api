package com.orion.infrastructure.cloudinary;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

    @Value("${spring.cloudinary.url}")
    private String cloudinaryUrl;

    @Bean
    public Cloudinary cloudinary() throws Exception {
        return new Cloudinary(cloudinaryUrl);
    }
}

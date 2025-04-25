package com.mtran.mvc.config.webconfig;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {
    @Bean
    public Cloudinary configKey() {
        Map<String,String> config = new HashMap<>();
        config.put("cloud_name", "dt42bg42j");
        config.put("api_key", "813611634967632");
        config.put("api_secret", "mMrceQeBzOJUTcJ6_5jzh_nn37Q");
        return new Cloudinary(config);
    }
}

package io.jaamong.recordMCI.api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        String[] allowedOrigins = {
                "http://localhost:5000",
                "http://127.0.0.1:5000",
        };

        registry.addMapping("/**")
                .allowedOrigins(allowedOrigins) // 허용할 출처
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}

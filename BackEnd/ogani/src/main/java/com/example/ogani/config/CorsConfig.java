package com.example.ogani.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        // Cấu hình các tùy chọn CORS cho tất cả các yêu cầu
        config.setAllowedOriginPatterns(List.of("*")); // Cho phép tất cả các origin
        config.addAllowedMethod("*"); // Cho phép tất cả các phương thức (GET, POST, PUT, DELETE, v.v.)
        config.addAllowedHeader("*"); // Cho phép tất cả các header
        config.setAllowCredentials(true); // Cho phép các credentials (cookies, xác thực qua HTTP)

        // Ánh xạ các tùy chọn CORS cho tất cả các URL
        source.registerCorsConfiguration("/**", config);

        // Trả về một CorsFilter
        return new CorsFilter(source);
    }
}

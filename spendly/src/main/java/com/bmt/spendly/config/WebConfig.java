package com.bmt.spendly.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // /images/uploads/** verrà servito da C:/SpendlyUploads
        registry.addResourceHandler("/images/uploads/**")
                .addResourceLocations("file:///C:/SpendlyUploads/");
    }
}

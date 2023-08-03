package com.grassshop.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.validation.Valid;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    // application.properties 에 설정한 uploadPath 프로퍼티 값을 읽어옴
    @Value("${uploadPath}")
    String uploadPath;
//
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**").
                addResourceLocations(uploadPath); // 로컬 컴퓨터에 저장된 파일을 읽어올 root 경로를 설정
    }
}
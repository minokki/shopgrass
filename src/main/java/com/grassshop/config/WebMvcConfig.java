package com.grassshop.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.validation.Valid;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${uploadPath}")//application.properties에 설정한 업로드 경로 읽어옴
    String uploadPath;

    @Override

    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/img/**") //images로 시작하는 경우 uploadPath에 설정한 폴더를 기준으로 파일 읽어옴
                .addResourceLocations(uploadPath); //컴퓨터에 저장된 파일을 읽어올 root경로
    }
}

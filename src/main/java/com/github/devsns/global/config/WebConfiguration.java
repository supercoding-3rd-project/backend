package com.github.devsns.global.config;

import org.springframework.http.HttpMethod;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedOrigins("*")
//                .allowedMethods(HttpMethod.GET.name(), HttpMethod.POST.name(), HttpMethod.PATCH.name(), HttpMethod.PUT.name(), HttpMethod.OPTIONS.name());
//    }
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000")  // 로컬 프런트엔드 서버 주소
                .allowedMethods(HttpMethod.GET.name(), HttpMethod.POST.name(), HttpMethod.PATCH.name(), HttpMethod.PUT.name(), HttpMethod.OPTIONS.name())
                .allowedHeaders("*")  // 모든 헤더 허용
                .allowCredentials(true);  // 크레덴셜 허용
    }

}
/**
 * 
 */
package com.eiisys.ipcc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.eiisys.ipcc.interceptor.LoginInterceptor;

/**
 * 实现WebMvcConfigurer，添加自定义Handler，Interceptor，ViewResolver，MessageConverter等
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Bean
    public LoginInterceptor loginInterceptor() {
        return new LoginInterceptor();
    }
    
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("*").allowedHeaders("*").allowedMethods("POST", "GET")
                .allowCredentials(true);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        // 前台页面url模式, 需要给哪些url添加拦截器
        String[] urlPatterns = {
                "/sip/**", // sip相关
                "/agent/**", //华为坐席相关
                "/voiceCall/**", //语音电话相关
        };

        registry.addInterceptor(loginInterceptor()).addPathPatterns(urlPatterns);
    }
}

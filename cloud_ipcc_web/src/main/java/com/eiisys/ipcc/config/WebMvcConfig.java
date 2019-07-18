/**
 * 
 */
package com.eiisys.ipcc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
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

    @Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsFilter(urlBasedCorsConfigurationSource);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("*").allowedHeaders("*").allowedMethods("*")
                .allowCredentials(true);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        // 前台页面url模式, 无需给哪些url添加拦截器
        String[] urlPatterns = {
                "/api/**", // 对外开放或者回调
                "/login", //登录
                "/tokenLogin", //token登录
                "/error", //错误页面
                "/webjars/**", //restfull页面
                "/swagger-ui.html",
                "/swagger-resources/**",
        };

        registry.addInterceptor(loginInterceptor()).addPathPatterns("/**").excludePathPatterns(urlPatterns);
    }
}

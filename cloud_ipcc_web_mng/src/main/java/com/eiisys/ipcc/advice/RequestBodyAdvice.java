package com.eiisys.ipcc.advice;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

/**
 * request body 去空格横切面 <br>
 * 这个类用来给所有controller接收到的request body里的String 对象做去除2边空格
 */
@RestControllerAdvice(basePackages = {
                                       "com.eiisys.ipcc.controller" })
public class RequestBodyAdvice extends RequestBodyAdviceAdapter {

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
            Class<? extends HttpMessageConverter<?>> converterType) {
        Field[] fields = body.getClass().getDeclaredFields();

        String tmp = null;

        for (Field field : fields) {
            if (String.class == field.getType()) {
                try {
                    field.setAccessible(true);
                    tmp = (String) field.get(body);

                    if (tmp != null && tmp.length() > 0) {
                        field.set(body, tmp.trim());
                    } else {
                        field.set(body, null);
                    }
                } catch (IllegalArgumentException | IllegalAccessException e) {

                }
            }
        }

        return body;
    }

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType,
            Class<? extends HttpMessageConverter<?>> converterType) {
        // 设成true，使本拦截器生效
        return true;
    }

}

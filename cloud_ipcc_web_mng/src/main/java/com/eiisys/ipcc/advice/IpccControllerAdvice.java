package com.eiisys.ipcc.advice;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.eiisys.ipcc.bean.GenericResponse;
import com.eiisys.ipcc.core.utils.ResponseUtils;
import com.eiisys.ipcc.exception.IpccException;
import com.google.common.base.Throwables;

import lombok.extern.slf4j.Slf4j;

/**
 * 针对所有RequestMapping请求处理的一些统一处理，包括String参数去空格以及统一的异常处理返回
 */
@RestControllerAdvice(basePackages = {
                                       "com.eiisys.ipcc.controller" })
@Slf4j
public class IpccControllerAdvice {

    /**
     * 应用到所有@RequestMapping注解方法，在其执行之前初始化数据绑定器
     * 
     * @param binder
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        // 对url请求参数，如果是String类型，则去除2边空格
        boolean emptyAsNull = true;// 字符串trim之后是""的话，返回null
        StringTrimmerEditor stringtrimmer = new StringTrimmerEditor(emptyAsNull);
        binder.registerCustomEditor(String.class, stringtrimmer);

    }
    
    /**
     * Controller中捕获到IpccException后的统一处理
     * @param e
     * @return
     */
    @ExceptionHandler(IpccException.class)
    public GenericResponse ipccExceptionHandler(IpccException e) {
        GenericResponse genericResponse = ResponseUtils.genErrorResponse(e);
        
        return genericResponse;
    }
    
    /**
     * Controller中捕获到Exception后的统一处理
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    public GenericResponse exceptionHandler(Exception e) {
        log.error("控制层发生系统异常: " + Throwables.getStackTraceAsString(e));
        
        String code = "";// 系统异常错误码
        GenericResponse genericResponse = ResponseUtils.genErrorResponse(code);
        
        return genericResponse;
    }
}

package com.eiisys.ipcc.core.utils;

import java.util.Locale;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import com.eiisys.ipcc.bean.GenericResponse;
import com.eiisys.ipcc.exception.IpccException;

import static com.eiisys.ipcc.constants.MsgConstants.*;

@Component
public class ResponseUtils {

	private static ResponseUtils responseUtils;
	
	@Autowired
	private MessageSource messageSource;
	
	@PostConstruct
	public void init() {
		responseUtils = this;
		responseUtils.messageSource = this.messageSource;
	}
	
	public static String getMessage(String code, String...args) {
	    if (StringUtils.isBlank(code) || args.length == 0) {
	        return "";
	    }
	    return responseUtils.messageSource.getMessage(code, args, Locale.CHINA);
	}
	
	public static GenericResponse genSuccessResponse(String code, String...args) {
		
		GenericResponse genericResponse = new GenericResponse();
		
		code = StringUtils.isEmpty(code) ? MSG_SUCCESS : code;
		args = args.length == 0 ? new String[] {""} : args;
		
		String message = responseUtils.messageSource.getMessage(code, args, Locale.CHINA);
		
		genericResponse.setCode(code);
		genericResponse.setMessage(message);
		genericResponse.setResult(true);
		
		return genericResponse;
	}
	
	public static GenericResponse genErrorResponse(String code, String...args) {
		GenericResponse genericResponse = new GenericResponse();
		
		String message = responseUtils.messageSource.getMessage(code, args, Locale.CHINA);
		
		genericResponse.setCode(code);
		genericResponse.setMessage(message);
		
		return genericResponse;
	}
	
    public static GenericResponse genErrorResponse(IpccException e) {
        GenericResponse genericResponse = new GenericResponse();

        genericResponse.setCode(e.getCode());
        genericResponse.setMessage(getMessage(e.getCode(), e.getArgs()));

        return genericResponse;
    }
    
    public static IpccException genIpccException(String code, String...args) {      
        return new IpccException(code, getMessage(code, args));
    }
}

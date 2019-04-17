package com.eiisys.ipcc.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.eiisys.ipcc.constants.IpccConstants;

import io.swagger.annotations.ApiOperation;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
	
//    /**
//     * 解决跨域问题
//     * @return
//     */
//    @Bean
//    public CorsFilter corsFilter() {
//        final UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
//        final CorsConfiguration corsConfiguration = new CorsConfiguration();
//        corsConfiguration.setAllowCredentials(true);
//        corsConfiguration.addAllowedOrigin("*");
//        corsConfiguration.addAllowedHeader("*");
//        corsConfiguration.addAllowedMethod("*");
//        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
//        return new CorsFilter(urlBasedCorsConfigurationSource);
//    }

	
	/**
	 * Swagger configuration method
	 * @return
	 */
	@Bean
	public Docket swaggerSettings() {
		String pathMapping="/";
		
		return new Docket(DocumentationType.SWAGGER_2)
				.groupName("资源中心")
				.useDefaultResponseMessages(false)
				.forCodeGeneration(false)
				.select()
				.apis(RequestHandlerSelectors.any())
				.apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
				.paths(PathSelectors.any()) // 有待研究
				.build()
				.apiInfo(apiInfo())
				.pathMapping(pathMapping)
				.globalOperationParameters(globalOperationParameters())
				;
	}

	/**
	 * Swagger UI configuration
	 * @return
	 */
//	@Bean
//	UiConfiguration uiConfig() {
//	    return UiConfigurationBuilder.builder()
//	        .deepLinking(true)
//	        .displayOperationId(false)
//	        .defaultModelsExpandDepth(1)
//	        .defaultModelExpandDepth(1)
//	        .defaultModelRendering(ModelRendering.EXAMPLE)
//	        .displayRequestDuration(false)
//	        .docExpansion(DocExpansion.NONE)
//	        .filter(false)
//	        .maxDisplayedTags(null)
//	        .tagsSorter(TagsSorter.ALPHA)
//	        .operationsSorter(null)
//	        .showExtensions(false)
//	        .validatorUrl(null)
//	        .build();
//	}
	
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title("联络中心后台管理系统 API")
				.description(initDescription())
				.version("1.0.0")
//				.licenseUrl("http://cn.bing.com")
//				.termsOfServiceUrl("https://www.baidu.com")
				.license("The Apache License, Version 2.0")
				.contact("联络中心开发团队")
				.build();
	}
	
	private String initDescription(){
		StringBuffer sb = new StringBuffer();
		
		sb.append("本页面用来测试华为云联络中心相关 接口文档功能")
			.append("<br/>")
			.append("<br/>");
		
		return sb.toString();
	}
	
	/**
	 * 设置全局参数变量
	 * @return
	 */
	private List<Parameter> globalOperationParameters() {
	    List<Parameter> pars = new ArrayList<>();
	    ParameterBuilder tokenPar = new ParameterBuilder();
	    tokenPar.name(IpccConstants.IPCC_ACCESS_TOKEN).description("普通令牌").modelRef(new ModelRef("string")).parameterType("header").required(false);
	    pars.add(tokenPar.build());
	    
	    return pars;
	}
}

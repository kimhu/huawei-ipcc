package com.eiisys.ipcc.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.web.context.support.StandardServletEnvironment;

@Configuration
// @AutoConfigureOrder
@PropertySources({
                   @PropertySource(value = "classpath:/config/${spring.profiles.active}/database.properties", ignoreResourceNotFound = true, encoding = "UTF-8"),
                   @PropertySource(value = "classpath:/config/${spring.profiles.active}/redis.properties", ignoreResourceNotFound = true, encoding = "UTF-8") })
public class PropertiesImportConfig {

    @Autowired
    private Environment env;


}

package com.ryan.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:dubbo.properties")
@ImportResource("classpath:dubbo-provider.xml")
public class DubboConfig {

}

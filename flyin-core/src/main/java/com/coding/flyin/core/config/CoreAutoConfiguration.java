package com.coding.flyin.core.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(FlyinConfigurerSupport.class)
public class CoreAutoConfiguration {}

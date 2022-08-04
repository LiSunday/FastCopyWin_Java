package com.sundayli.fastcopywin.config;


import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan(value = "com.sundayli.fastcopywin.service")
@Import(DatabaseConfig.class)
public class SpringConfig {
}
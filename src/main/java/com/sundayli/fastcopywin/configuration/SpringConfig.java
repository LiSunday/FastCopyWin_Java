package com.sundayli.fastcopywin.configuration;


import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@ComponentScan(value = "com.sundayli.fastcopywin.service")
@Import(DatabaseConfig.class)
@EnableAsync
public class SpringConfig {
}
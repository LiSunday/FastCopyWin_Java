package com.example.fastcopywin.config;


import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan(value = "com.example.fastcopywin.service")
public class SpringConfig {
}

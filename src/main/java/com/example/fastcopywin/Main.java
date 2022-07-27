package com.example.fastcopywin;

import com.example.fastcopywin.config.SpringConfig;
import javafx.application.Application;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {

  public static ApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);
  public static void main(String[] args) {
    Application.launch(MainApplication.class);
  }
}

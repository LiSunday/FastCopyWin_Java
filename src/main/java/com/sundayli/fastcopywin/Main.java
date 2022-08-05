package com.sundayli.fastcopywin;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.sundayli.fastcopywin.config.SpringConfig;
import java.util.logging.Logger;
import javafx.application.Application;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
  public static final ApplicationContext CONTEXT = new AnnotationConfigApplicationContext(SpringConfig.class);
  private static final Logger log = Logger.getLogger(Main.class.toString());

  static {
    try {
      // 注册钩子函数
      GlobalScreen.registerNativeHook();
    }
    catch (NativeHookException ex) {
      log.severe("There was a problem registering the native hook.");
      log.severe(ex.getMessage());
      System.exit(1);
    }
  }

  public static void main(String[] args) {
    Application.launch(MainApplication.class);
  }

}

package com.example.fastcopywin;

import com.example.fastcopywin.controller.MainController;
import com.example.fastcopywin.listener.KeyBoardGlobalListener;
import com.example.fastcopywin.listener.MouseInputGlobalListener;
import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.Clipboard;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {
  @Override
  public void start(Stage stage) throws IOException, InterruptedException {
    // 设置隐藏窗口后 不终止 fx 线程
    Platform.setImplicitExit(false);

    // 主界面
    FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("main-view.fxml"));
    Scene scene = new Scene(fxmlLoader.load(), 320, 240);
    stage.setTitle("FastCopyWin");
    stage.setScene(scene);
    Thread fx = Thread.currentThread();

    // 只能初始化一次
    intGlobalListener();
    // 初始化全局键盘监听
    KeyBoardGlobalListener keyBoardGlobalListener = KeyBoardGlobalListener.getSingleInstance();
    // 初始化全局鼠标监听
    MouseInputGlobalListener mouseInputGlobalListener = MouseInputGlobalListener.getSingleInstance();

    // 注册回调事件 显示菜单栏
    keyBoardGlobalListener.registerCustomKeyAfterReturnEvent(29, NativeKeyEvent.VC_M, () -> {
      System.out.println(fx.isAlive());
      Platform.runLater(() -> {
        stage.setX(mouseInputGlobalListener.getMouseX());
        stage.setY(mouseInputGlobalListener.getMouseY());
        stage.show();
        // TODO 应该有更好的方法强制窗体获取焦点
        MainController controller = fxmlLoader.getController();
        controller.welcomeText.requestFocus();
      });
    });
    // 注册回调事件 隐藏菜单栏
    mouseInputGlobalListener.registerCustomMouseKeyAfterReturnEvent(1, () -> {
      Platform.runLater(() -> {
        if (scene.getFocusOwner() != null && (!scene.getFocusOwner().isFocused())) {
          stage.hide();
        }
      });
    });
  }

  private void intGlobalListener() {
    try {
      // 注册钩子函数
      GlobalScreen.registerNativeHook();
    }
    catch (NativeHookException ex) {
      System.err.println("There was a problem registering the native hook.");
      System.err.println(ex.getMessage());

      System.exit(1);
    }
  }

  public static void main(String[] args) {
    launch();
  }
}
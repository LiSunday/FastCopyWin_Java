package com.example.fastcopywin;

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
  public void start(Stage stage) throws IOException {
    // 主界面
    FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("main-view.fxml"));
    Scene scene = new Scene(fxmlLoader.load(), 320, 240);
    stage.setTitle("FastCopyWin");
    stage.setScene(scene);

    Clipboard clipboard = Clipboard.getSystemClipboard();
    intGlobalListener();
    KeyBoardGlobalListener keyBoardGlobalListener = KeyBoardGlobalListener.getSingleInstance();
    MouseInputGlobalListener mouseInputGlobalListener = MouseInputGlobalListener.getSingleInstance();
    keyBoardGlobalListener.registerCopyAfterReturnEvent(() -> {
      Platform.runLater(() -> {
        stage.setTitle(clipboard.getString());
      });
    });
    keyBoardGlobalListener.registerCustomKeyAfterReturnEvent(29, NativeKeyEvent.VC_M, () -> {
      Platform.runLater(() -> {
        stage.setX(mouseInputGlobalListener.getMouseX());
        stage.setY(mouseInputGlobalListener.getMouseY());
        stage.show();
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
package com.example.fastcopywin;

import com.example.fastcopywin.controller.MainController;
import com.example.fastcopywin.listener.KeyBoardGlobalListener;
import com.example.fastcopywin.listener.MouseInputGlobalListener;
import com.example.fastcopywin.service.RecordDataService;
import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.Clipboard;
import javafx.scene.input.DataFormat;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MainApplication extends Application {

  @Override
  public void start(Stage stage) throws IOException {
    // 设置隐藏窗口后 不终止 fx 线程
    Platform.setImplicitExit(false);

    // 主界面
    FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("main-view.fxml"));
    Scene scene = new Scene(fxmlLoader.load(), 200, 600);
    stage.setTitle("FastCopyWin");

    stage.setScene(scene);
    stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/red_heart.png"))));

    MainController controller = fxmlLoader.getController();

    // 获取系统剪切板
    Clipboard clipboard = Clipboard.getSystemClipboard();
    // 只能初始化一次
    intGlobalListener();
    // 初始化全局键盘监听
    KeyBoardGlobalListener keyBoardGlobalListener = KeyBoardGlobalListener.getSingleInstance();
    // 初始化全局鼠标监听
    MouseInputGlobalListener mouseInputGlobalListener = MouseInputGlobalListener.getSingleInstance();

    // 注册键盘回调事件 显示菜单栏
    keyBoardGlobalListener.registerCustomKeyAfterReturnEvent(29, NativeKeyEvent.VC_M, () -> Platform.runLater(() -> {
      stage.setX(mouseInputGlobalListener.getMouseX());
      stage.setY(mouseInputGlobalListener.getMouseY());
      stage.show();
      stage.toFront();
      stage.setAlwaysOnTop(true);
      // 刷新数据 & 强制焦点在列表第一项
      controller.dataList.setItems(FXCollections.observableList(Main.context.getBean(RecordDataService.class).getRecordDataTopN(10)));
      controller.dataList.getFocusModel().focus(0);
    }));
    // 注册鼠标回调事件 隐藏菜单栏
    mouseInputGlobalListener.registerCustomMouseKeyAfterReturnEvent(1, () -> Platform.runLater(() -> {
      if (scene.getFocusOwner() != null && (!scene.getFocusOwner().isFocused())) {
        stage.hide();
      }
    }));
    // 注册键盘回调事件 将复制的东西持久化保存一下
    keyBoardGlobalListener.registerCustomKeyAfterReturnEvent(29, NativeKeyEvent.VC_C, () -> {
      Platform.runLater(() -> {
        String data = clipboard.getString();
        if (StringUtils.isNotBlank(data)) {
          Main.context.getBean(RecordDataService.class).saveRecord(data);
        }
      });
    });
    // 注册键盘回调事件 将选中的内容放入系统剪切板
    keyBoardGlobalListener.registerCustomKeyAfterReturnEvent(NativeKeyEvent.VC_ENTER, () -> Platform.runLater(() -> {
      if (controller.dataList.isFocused()) {
        String data = controller.dataList.getFocusModel().getFocusedItem();
        Map<DataFormat, Object> dataMap = new HashMap<>();
        dataMap.put(DataFormat.PLAIN_TEXT, data);
        clipboard.setContent(dataMap);
        Main.context.getBean(RecordDataService.class).saveRecord(data);
        stage.hide();
      }
    }));
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
}
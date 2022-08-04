package com.sundayli.fastcopywin;

import com.sundayli.fastcopywin.controller.MainController;
import com.sundayli.fastcopywin.listener.KeyBoardGlobalListener;
import com.sundayli.fastcopywin.listener.MouseInputGlobalListener;
import com.sundayli.fastcopywin.model.RecordData;
import com.sundayli.fastcopywin.model.eum.DataFormatEnum;
import com.sundayli.fastcopywin.service.RecordDataService;
import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.sundayli.fastcopywin.system.CustomClipboard;
import com.sundayli.fastcopywin.system.model.ClipboardData;
import com.sundayli.fastcopywin.util.ImageUtils;
import java.awt.datatransfer.DataFlavor;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.robot.Robot;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

public class MainApplication extends Application {
  private static final Logger log = Logger.getLogger(MainApplication.class.toString());

  @Override
  public void start(Stage stage) throws IOException {
    // 设置隐藏窗口后 不终止 fx 线程
    Platform.setImplicitExit(false);

    Robot robot = new Robot();

    // 主界面
    FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("main-view.fxml"));
    Scene scene = new Scene(fxmlLoader.load(), 250, 600);
    stage.setTitle("FastCopyWin");

    stage.setScene(scene);
    stage.setAlwaysOnTop(true);
    stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/red_heart.png"))));

    MainController controller = fxmlLoader.getController();

    // 只能初始化一次
    intGlobalListener();
    // 初始化全局键盘监听
    KeyBoardGlobalListener keyBoardGlobalListener = KeyBoardGlobalListener.getSingleInstance();
    // 初始化全局鼠标监听
    MouseInputGlobalListener mouseInputGlobalListener = MouseInputGlobalListener.getSingleInstance();

    // 注册键盘回调事件 显示菜单栏
    keyBoardGlobalListener.registerCustomKeyAfterReturnEvent(29, NativeKeyEvent.VC_M, () -> Platform.runLater(() -> {
      double x = robot.getMouseX();
      double y = robot.getMouseY();
      stage.setX(robot.getMouseX());
      stage.setY(robot.getMouseY());
      stage.show();
      stage.requestFocus();

      // 刷新数据 & 强制将焦点放在第一项
      // TODO [性能优化] 没有必要更新全量数据 也没有必要从文件获取全量数据 不过暂时没有瓶颈 先不管
      List<Object> dataList = new ArrayList<>();
      for (RecordData recordData : Main.SPRING_CONTEXT.getBean(RecordDataService.class).getRecordDataTopN(10)) {
        if (recordData.getDataFormat() == DataFormatEnum.PLAIN_TEXT) {
          dataList.add(recordData.getData());
        } else if (recordData.getDataFormat() == DataFormatEnum.IMAGE) {
          ImageView imageView = new ImageView(ImageUtils.toImage(recordData.getData()));
          double w = imageView.getImage().getWidth();
          double h = imageView.getImage().getHeight();
          // TODO: 2022/8/4 sundayli 后面抽成设置 这里用 40 是把显示区域做个缩放 图片太大不美观 也不方便
          imageView.setFitHeight(40);
          imageView.setFitWidth(w / h * 40);
          dataList.add(imageView);
        }
      }
      controller.dataList.setItems(FXCollections.observableList(dataList));
      controller.dataList.getSelectionModel().selectFirst();
      // TODO 非常 hack 的写法 用来对付有些电脑 requestFocus 不能正确获取到焦点
      robot.mouseMove(x + 5, y + 5);
      robot.mouseClick(MouseButton.PRIMARY);
      robot.mouseMove(x, y);
    }));
    // 注册鼠标回调事件 隐藏菜单栏
    mouseInputGlobalListener.registerCustomMouseKeyAfterReturnEvent(1, () -> Platform.runLater(() -> {
      if (scene.getRoot().isVisible() && !stage.isFocused()) {
        stage.hide();
      }
    }));
    // 监听剪切板变化 将复制的东西持久化保存一下
    CustomClipboard.getInstance().addListener(clipboardData -> {
      RecordData recordData = null;
      if (clipboardData.isImage()) {
        recordData = new RecordData();
        recordData.setData(ImageUtils.toString(clipboardData.getBufferedImage()));
        recordData.setDataFormat(DataFormatEnum.IMAGE);
      } else if (clipboardData.isString()) {
        recordData = new RecordData();
        recordData.setData(clipboardData.getStrData());
        recordData.setDataFormat(DataFormatEnum.PLAIN_TEXT);
      }
      if (recordData != null) {
        Main.SPRING_CONTEXT.getBean(RecordDataService.class).saveRecord(recordData);
      }
    });
    // 注册键盘回调事件 将选中的内容放入系统剪切板
    keyBoardGlobalListener.registerCustomKeyAfterReturnEvent(NativeKeyEvent.VC_ENTER, () -> Platform.runLater(() -> {
      if (controller.dataList.isFocused()) {
        Object data = controller.dataList.getFocusModel().getFocusedItem();
        if (data instanceof String){
          ClipboardData clipboardData = new ClipboardData();
          clipboardData.setDataFlavor(DataFlavor.stringFlavor);
          clipboardData.setStrData((String) data);
          CustomClipboard.getInstance().coverClipboardData(clipboardData);
        } else if (data instanceof ImageView) {
          ImageView imageView = (ImageView) data;
          Image image = imageView.getImage();
          ClipboardData clipboardData = new ClipboardData();
          clipboardData.setDataFlavor(DataFlavor.imageFlavor);
          clipboardData.setBufferedImage(ImageUtils.toBufferedImage(image));
          CustomClipboard.getInstance().coverClipboardData(clipboardData);
        }
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
      log.severe("There was a problem registering the native hook.");
      log.severe(ex.getMessage());
      System.exit(1);
    }
  }
}
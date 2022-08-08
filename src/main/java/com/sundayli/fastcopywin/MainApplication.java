package com.sundayli.fastcopywin;

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.sundayli.fastcopywin.controller.ActionTypeEnum;
import com.sundayli.fastcopywin.controller.BaseController;
import com.sundayli.fastcopywin.listener.KeyBoardGlobalListener;
import com.sundayli.fastcopywin.listener.MouseInputGlobalListener;
import com.sundayli.fastcopywin.model.RecordData;
import com.sundayli.fastcopywin.model.eum.DataFormatEnum;
import com.sundayli.fastcopywin.service.RecordDataService;
import com.sundayli.fastcopywin.system.CustomClipboard;
import com.sundayli.fastcopywin.util.ImageUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.robot.Robot;
import javafx.stage.Stage;
import org.apache.commons.lang3.tuple.Pair;

public class MainApplication extends Application {

  private final List<BaseController> controllers = Collections.synchronizedList(new ArrayList<>());
  private Robot robot = null;

  @Override
  public void start(Stage stage) throws IOException {
    // 设置隐藏窗口后 不终止 fx 线程
    Platform.setImplicitExit(false);
    robot = new Robot();

    FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("main-view.fxml"));
    Scene scene = new Scene(fxmlLoader.load(), 250, 600);
    stage.setTitle("FastCopyWin");
    stage.setScene(scene);
    stage.setAlwaysOnTop(true);
    stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/red_heart.png"))));
    stage.show();
    controllers.add(fxmlLoader.getController());
    stage.hide();
    enableListener(scene, stage);
  }

  private void enableListener(Scene scene, Stage stage) {
    keyBoardListener(stage);
    mouseListener(scene, stage);
    clipBoardListener();
  }

  private void clipBoardListener() {
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
        Main.CONTEXT.getBean(RecordDataService.class).saveRecord(recordData);
        RecordData data = recordData;
        Platform.runLater(() -> controllers.forEach(controller -> controller.triggerAction(ActionTypeEnum.UPDATE, data)));
      }
    });
  }

  private void mouseListener(Scene scene, Stage stage) {
    MouseInputGlobalListener mouseInputGlobalListener = MouseInputGlobalListener.getSingleInstance();
    mouseInputGlobalListener.registerCustomMouseKeyAfterReturnEvent(1, () -> Platform.runLater(() -> {
      if (scene.getRoot().isVisible() && !stage.isFocused()) {
        stage.hide();
      }
      if (!stage.isShowing()) {
        Main.RESOURCE.set(Pair.of(robot.getMouseX(), robot.getMouseY()));
      }
    }));
  }

  private void keyBoardListener(Stage stage) {
    KeyBoardGlobalListener listener = KeyBoardGlobalListener.getSingleInstance();

    listener.registerCustomKeyAfterReturnEvent(NativeKeyEvent.VC_CONTROL, NativeKeyEvent.VC_M, () -> Platform.runLater(() -> {
      double x = robot.getMouseX();
      double y = robot.getMouseY();
      stage.setX(robot.getMouseX());
      stage.setY(robot.getMouseY());
      stage.show();
      stage.requestFocus();

      // 通知各个 controller 为 show 做出相应动作
      controllers.forEach(baseController -> baseController.triggerAction(ActionTypeEnum.SHOW));

      // TODO 非常 hack 的写法 用来对付有些电脑 requestFocus 不能正确获取到焦点
      robot.mouseMove(x + 5, y + 5);
      robot.mouseClick(MouseButton.PRIMARY);
      robot.mouseMove(x, y);
    }));

    listener.registerCustomKeyAfterReturnEvent(NativeKeyEvent.VC_ENTER, () -> Platform.runLater(() -> {
      if (stage.isFocused()) {
        // 通知各个 controller 为 回车事件做出相应动作
        controllers.forEach(controller -> controller.triggerAction(ActionTypeEnum.ENTER));
        stage.hide();

        // 对数据进行回填
        Pair<Double, Double> pair = Main.RESOURCE.get();
        if (pair == null) return;
        double x = robot.getMouseX();
        double y = robot.getMouseY();
        robot.mouseMove(pair.getLeft(), pair.getRight());
        robot.mouseClick(MouseButton.PRIMARY);
        robot.keyPress(KeyCode.ALT);
        robot.keyPress(KeyCode.V);
        robot.keyRelease(KeyCode.V);
        robot.keyRelease(KeyCode.ALT);
        robot.mouseMove(x, y);
      }
    }));
  }
}
package com.example.fastcopywin.listener;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;

import java.util.EnumMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;

public class KeyBoardGlobalListener implements NativeKeyListener {

  private static KeyBoardGlobalListener keyBoardGlobalListener;


  private Map<KeyEnum, ListenerOperation> keyListenerAfterReturnEventMap;
  private Queue<KeyEnum> keyCombinationQueue;

  private volatile int currentKeyCode;

  /**
   * 获取单例全局键盘监听
   * @return
   */
  public static KeyBoardGlobalListener getSingleInstance() {
    if (keyBoardGlobalListener == null) {
      synchronized (KeyBoardGlobalListener.class) {
        if (keyBoardGlobalListener == null) {
          keyBoardGlobalListener = new KeyBoardGlobalListener();
        }
      }
    }
    return keyBoardGlobalListener;
  }

  private KeyBoardGlobalListener() {
    try {
      // 注册钩子函数
      GlobalScreen.registerNativeHook();
    }
    catch (NativeHookException ex) {
      System.err.println("There was a problem registering the native hook.");
      System.err.println(ex.getMessage());

      System.exit(1);
    }

    // 添加监听项
    GlobalScreen.addNativeKeyListener(this);
    // 初始化监听事件映射
    keyListenerAfterReturnEventMap = new EnumMap<>(KeyEnum.class);
    keyCombinationQueue = new LinkedList<>();
    currentKeyCode = -1;
  }

  public void registerCopyAfterReturnEvent(ListenerOperation listenerOperation) {
    keyListenerAfterReturnEventMap.put(KeyEnum.COPY_KEY_COMBINATION, listenerOperation);
  }

  public void nativeKeyPressed(NativeKeyEvent e) {
    // 收集已经定义好的组合键
    String combinationStr = currentKeyCode + "_" + e.getKeyCode();
    Optional.ofNullable(KeyEnum.keyOf(combinationStr)).ifPresent(keyCombinationQueue::add);
    currentKeyCode = e.getKeyCode();
  }

  public void nativeKeyReleased(NativeKeyEvent e) {
    // 处理所有触发的组合键事件
    while (!keyCombinationQueue.isEmpty()) {
      KeyEnum keyEnum = keyCombinationQueue.poll();
      Optional.ofNullable(keyListenerAfterReturnEventMap.get(keyEnum)).ifPresent(ListenerOperation::operation);
    }
  }

  public static void main(String[] args) {
    KeyBoardGlobalListener singleInstance = KeyBoardGlobalListener.getSingleInstance();
    singleInstance.registerCopyAfterReturnEvent(() -> {
      System.out.println("组合键触发成功！！！");
    });
  }

  enum KeyEnum {
    COPY_KEY_COMBINATION("29_46", "复制组合键");

    String key;
    String describe;

    KeyEnum(String value, String describe) {
      this.key = value;
      this.describe = describe;
    }

    static KeyEnum keyOf(String value) {
      switch (value) {
        case "29_46":
          return COPY_KEY_COMBINATION;
        default:
          return null;
      }
    }
  }
}
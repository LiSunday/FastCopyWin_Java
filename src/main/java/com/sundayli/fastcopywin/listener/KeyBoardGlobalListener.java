package com.sundayli.fastcopywin.listener;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;

public class KeyBoardGlobalListener implements NativeKeyListener {

  private static KeyBoardGlobalListener keyBoardGlobalListener;


  private Map<String, List<ListenerOperation>> keyListenerAfterReturnEventMap;
  private Queue<String> keyCombinationQueue;

  private volatile int currentKeyCode;

  /**
   * 获取单例全局键盘监听
   * @return
   */
  public static synchronized KeyBoardGlobalListener getSingleInstance() {
    if (keyBoardGlobalListener == null) {
      keyBoardGlobalListener = new KeyBoardGlobalListener();
    }
    return keyBoardGlobalListener;
  }

  private KeyBoardGlobalListener() {
    // 添加监听项
    GlobalScreen.addNativeKeyListener(this);
    // 初始化监听事件映射
    keyListenerAfterReturnEventMap = new HashMap<>();
    keyCombinationQueue = new LinkedList<>();
    currentKeyCode = -1;
  }

  public void registerCopyAfterReturnEvent(ListenerOperation listenerOperation) {
    registerCustomKeyAfterReturnEvent(29, 46, listenerOperation);
  }

  public void registerCustomKeyAfterReturnEvent(int preKeyCode, int postKeyCode, ListenerOperation listenerOperation) {
    keyListenerAfterReturnEventMap.computeIfAbsent(String.format("%d_%d", preKeyCode, postKeyCode), k -> new ArrayList<>()).add(listenerOperation);
  }

  public void registerCustomKeyAfterReturnEvent(int code, ListenerOperation listenerOperation) {
    keyListenerAfterReturnEventMap.computeIfAbsent(String.valueOf(code), k -> new ArrayList<>()).add(listenerOperation);
  }

  @Override
  public void nativeKeyPressed(NativeKeyEvent e) {
    // 收集触发的组合键
    // TODO 后续可以支持 N 组合键
    keyCombinationQueue.add(String.format("%d_%d", currentKeyCode, e.getKeyCode()));
    keyCombinationQueue.add(String.valueOf(e.getKeyCode()));
    currentKeyCode = e.getKeyCode();
  }

  @Override
  public void nativeKeyReleased(NativeKeyEvent e) {
    // 处理所有触发的组合键事件
    while (!keyCombinationQueue.isEmpty()) {
      String key = keyCombinationQueue.poll();
      Optional.ofNullable(keyListenerAfterReturnEventMap.get(key)).ifPresent(list -> list.forEach(ListenerOperation::operation));
    }
  }
}
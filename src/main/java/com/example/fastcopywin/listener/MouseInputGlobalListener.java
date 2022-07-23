package com.example.fastcopywin.listener;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.mouse.NativeMouseEvent;
import com.github.kwhat.jnativehook.mouse.NativeMouseInputListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class MouseInputGlobalListener implements NativeMouseInputListener {

  private static MouseInputGlobalListener mouseInputGlobalListener;


  private Map<String, List<ListenerOperation>> listenersMap;

  private int mouseX;
  private int mouseY;

  private MouseInputGlobalListener() {
    // 添加监听项
    GlobalScreen.addNativeMouseMotionListener(this);
    GlobalScreen.addNativeMouseListener(this);
    listenersMap = new HashMap<>();
  }

  public static MouseInputGlobalListener getSingleInstance() {
    if (mouseInputGlobalListener == null) {
      synchronized (MouseInputGlobalListener.class) {
        if (mouseInputGlobalListener == null) {
          mouseInputGlobalListener = new MouseInputGlobalListener();
        }
      }
    }
    return mouseInputGlobalListener;
  }

  @Override
  public void nativeMouseMoved(NativeMouseEvent e) {
    this.mouseX = e.getX();
    this.mouseY = e.getY();
  }

  @Override
  public void nativeMouseReleased(NativeMouseEvent e) {
    Optional.ofNullable(listenersMap.get(String.valueOf(e.getButton()))).ifPresent(list -> list.forEach(ListenerOperation::operation));
  }

  public void registerCustomMouseKeyAfterReturnEvent(int code, ListenerOperation listenerOperation) {
    listenersMap.computeIfAbsent(String.valueOf(code), k -> new ArrayList<>()).add(listenerOperation);
  }

  public int getMouseX() {
    return mouseX;
  }

  public int getMouseY() {
    return mouseY;
  }

  public static void main(String[] args) {
    MouseInputGlobalListener singleInstance = getSingleInstance();
    System.out.println(singleInstance.getMouseX());
    System.out.println(singleInstance.getMouseY());
  }
}

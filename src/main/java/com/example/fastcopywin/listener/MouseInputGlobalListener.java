package com.example.fastcopywin.listener;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.mouse.NativeMouseEvent;
import com.github.kwhat.jnativehook.mouse.NativeMouseInputListener;

public class MouseInputGlobalListener implements NativeMouseInputListener {

  private static MouseInputGlobalListener mouseInputGlobalListener;

  private int mouseX;
  private int mouseY;

  private MouseInputGlobalListener() {
    // 添加监听项
    GlobalScreen.addNativeMouseMotionListener(this);
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

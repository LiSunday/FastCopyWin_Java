package com.example.fastcopywin.listener;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.mouse.NativeMouseEvent;
import com.github.kwhat.jnativehook.mouse.NativeMouseInputListener;
import org.apache.commons.lang3.tuple.Pair;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class MouseInputGlobalListener implements NativeMouseInputListener {

  private static MouseInputGlobalListener mouseInputGlobalListener;


  private Map<String, List<ListenerOperation>> listenersMap;

  private volatile Point point;

  private MouseInputGlobalListener() {
    // 添加监听项
    GlobalScreen.addNativeMouseMotionListener(this);
    GlobalScreen.addNativeMouseListener(this);
    listenersMap = new HashMap<>();
  }

  public synchronized static MouseInputGlobalListener getSingleInstance() {
    if (mouseInputGlobalListener == null) {
      mouseInputGlobalListener = new MouseInputGlobalListener();
    }
    return mouseInputGlobalListener;
  }

  @Override
  public void nativeMouseMoved(NativeMouseEvent e) {
    this.point = e.getPoint();
  }

  @Override
  public void nativeMouseReleased(NativeMouseEvent e) {
    Optional.ofNullable(listenersMap.get(String.valueOf(e.getButton()))).ifPresent(list -> list.forEach(ListenerOperation::operation));
  }

  public void registerCustomMouseKeyAfterReturnEvent(int code, ListenerOperation listenerOperation) {
    listenersMap.computeIfAbsent(String.valueOf(code), k -> new ArrayList<>()).add(listenerOperation);
  }

  public Point getPoint() {
    return point == null ? new Point(0, 0) : point;
  }
}

package com.sundayli.fastcopywin.controller;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

public abstract class BaseController {

  Map<ActionTypeEnum, ControlAction> controlActionMap = new EnumMap<>(ActionTypeEnum.class);

  public void addAction(ActionTypeEnum actionTypeEnum, ControlAction controlAction) {
    controlActionMap.put(actionTypeEnum, controlAction);
  }

  public void triggerAction(ActionTypeEnum actionTypeEnum, Object... data) {
    Optional.ofNullable(controlActionMap.get(actionTypeEnum)).ifPresent(controlAction -> controlAction.action(data));
  }
}

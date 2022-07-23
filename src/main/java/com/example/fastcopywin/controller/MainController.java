package com.example.fastcopywin.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.Clipboard;

public class MainController {
  @FXML
  public Label welcomeText;

  @FXML
  protected void onHelloButtonClick() {
    Clipboard clipboard = Clipboard.getSystemClipboard();
    welcomeText.setText(clipboard.getString());
  }
}
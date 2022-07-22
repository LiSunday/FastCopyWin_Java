package com.example.fastcopywin.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.Clipboard;

public class HelloController {
  @FXML
  private Label welcomeText;

  @FXML
  protected void onHelloButtonClick() {
    Clipboard clipboard = Clipboard.getSystemClipboard();
    welcomeText.setText(clipboard.getString());
  }
}
module com.example.fastcopywin {
  requires javafx.controls;
  requires javafx.fxml;
  requires javafx.web;

  requires org.controlsfx.controls;
  requires com.dlsc.formsfx;
  requires org.kordamp.ikonli.javafx;
  requires org.kordamp.bootstrapfx.core;
  requires eu.hansolo.tilesfx;
  requires com.github.kwhat.jnativehook;

  opens com.example.fastcopywin to javafx.fxml;
  exports com.example.fastcopywin;
  exports com.example.fastcopywin.controller;
  opens com.example.fastcopywin.controller to javafx.fxml;
}
module com.example.fastcopywin {
  requires javafx.controls;
  requires javafx.fxml;
  requires javafx.web;

  requires org.controlsfx.controls;
  requires com.dlsc.formsfx;
  requires org.kordamp.ikonli.javafx;
  requires org.kordamp.bootstrapfx.core;
  requires com.github.kwhat.jnativehook;

  requires spring.beans;
  requires spring.context;
  requires spring.core;

  requires org.apache.commons.lang3;

  exports com.example.fastcopywin;
  exports com.example.fastcopywin.controller;
  exports com.example.fastcopywin.config;
  exports com.example.fastcopywin.service;
  exports com.example.fastcopywin.listener;

  opens com.example.fastcopywin to javafx.fxml;
  opens com.example.fastcopywin.controller to javafx.fxml;
  opens com.example.fastcopywin.config to spring.core;
  opens com.example.fastcopywin.service to spring.core;

}
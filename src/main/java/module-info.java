module com.example.fastcopywin {
  requires java.sql;
  requires java.naming;
  requires java.base;

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
  requires spring.jdbc;
  requires spring.tx;
  requires spring.aop;
  requires spring.expression;
  requires spring.jcl;

  requires spring.data.jdbc;
  requires spring.data.relational;
  requires spring.data.commons;

  requires org.apache.commons.lang3;

  requires com.h2database;

  exports com.example.fastcopywin;
  exports com.example.fastcopywin.controller;
  exports com.example.fastcopywin.config;
  exports com.example.fastcopywin.service;
  exports com.example.fastcopywin.listener;
  exports com.example.fastcopywin.repository;
  exports com.example.fastcopywin.model;

  opens com.example.fastcopywin to javafx.fxml;
  opens com.example.fastcopywin.controller to javafx.fxml;
  opens com.example.fastcopywin.config to java.base, spring.beans, spring.context, spring.core, spring.jdbc, spring.tx, spring.aop, spring.expression, spring.jcl, spring.data.relational, spring.data.jdbc, spring.data.commons;
  opens com.example.fastcopywin.service to spring.core;
  opens com.example.fastcopywin.repository to spring.beans, spring.context, spring.core, spring.jdbc, spring.tx, spring.aop, spring.expression, spring.jcl, spring.data.relational, spring.data.jdbc, spring.data.commons;
  opens com.example.fastcopywin.model to spring.beans, spring.context, spring.core, spring.jdbc, spring.tx, spring.aop, spring.expression, spring.jcl, spring.data.relational, spring.data.jdbc, spring.data.commons;

}
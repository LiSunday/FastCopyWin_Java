module com.sundayli.fastcopywin {
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

  exports com.sundayli.fastcopywin;
  exports com.sundayli.fastcopywin.controller;
  exports com.sundayli.fastcopywin.config;
  exports com.sundayli.fastcopywin.service;
  exports com.sundayli.fastcopywin.listener;
  exports com.sundayli.fastcopywin.repository;
  exports com.sundayli.fastcopywin.model;

  opens com.sundayli.fastcopywin to javafx.fxml;
  opens com.sundayli.fastcopywin.controller to javafx.fxml;
  opens com.sundayli.fastcopywin.config to java.base, spring.beans, spring.context, spring.core, spring.jdbc, spring.tx, spring.aop, spring.expression, spring.jcl, spring.data.relational, spring.data.jdbc, spring.data.commons;
  opens com.sundayli.fastcopywin.service to javafx.fxml, spring.core;
  opens com.sundayli.fastcopywin.repository to spring.beans, spring.context, spring.core, spring.jdbc, spring.tx, spring.aop, spring.expression, spring.jcl, spring.data.relational, spring.data.jdbc, spring.data.commons;
  opens com.sundayli.fastcopywin.model to javafx.fxml, spring.beans, spring.context, spring.core, spring.jdbc, spring.tx, spring.aop, spring.expression, spring.jcl, spring.data.relational, spring.data.jdbc, spring.data.commons;

}
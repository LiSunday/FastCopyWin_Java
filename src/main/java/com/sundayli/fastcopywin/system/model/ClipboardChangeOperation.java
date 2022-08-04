package com.sundayli.fastcopywin.system.model;

@FunctionalInterface
public interface ClipboardChangeOperation {
  void operation(ClipboardData clipboardData);
}

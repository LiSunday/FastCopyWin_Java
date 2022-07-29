package com.example.fastcopywin.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table(RecordData.TABLE_NAME)
public class RecordData {
  public static final String TABLE_NAME = "RECORD_DATA";

  @Id
  Integer id;
  String data;

  public static String getCreateTableSql() {
    return "create table if not exists " + TABLE_NAME
      + "("
      + "id int primary key auto_increment, "
      + "data TEXT "
      + ")";
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getData() {
    return data;
  }

  public void setData(String data) {
    this.data = data;
  }
}

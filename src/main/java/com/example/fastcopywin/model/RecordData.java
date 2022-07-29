package com.example.fastcopywin.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("RECORD_DATA")
public class RecordData {

  @Id
  Integer id;
  String data;

  public static String getCreateTableSql() {
    return "create table if not exists record_data\n"
      + "(\n"
      + "id int primary key auto_increment,\n"
      + "data TEXT\n"
      + ")\n";
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

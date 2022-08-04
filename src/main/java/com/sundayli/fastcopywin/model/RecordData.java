package com.sundayli.fastcopywin.model;

import com.sundayli.fastcopywin.model.eum.DataFormatEnum;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table(RecordData.TABLE_NAME)
public class RecordData {
  public static final String TABLE_NAME = "RECORD_DATA";

  @Id
  Integer id;

  String data;

  DataFormatEnum dataFormat;

  public static String getCreateTableSql() {
    return "create table if not exists " + TABLE_NAME
      + "("
      + "id int primary key auto_increment, "
      + "data TEXT, "
      + "data_format VARCHAR(20)"
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

  public DataFormatEnum getDataFormat() {
    return dataFormat;
  }

  public void setDataFormat(DataFormatEnum dataFormat) {
    this.dataFormat = dataFormat;
  }

  @Override
  public String toString() {
    if (dataFormat == DataFormatEnum.PLAIN_TEXT) {
      return data;
    }
    return super.toString();
  }
}

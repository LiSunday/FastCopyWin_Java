package com.sundayli.fastcopywin.system.model;

import java.awt.datatransfer.DataFlavor;
import java.awt.image.BufferedImage;

public class ClipboardData {

  String strData;

  BufferedImage bufferedImage;

  DataFlavor dataFlavor;

  public boolean isString() {
    return dataFlavor.equals(DataFlavor.stringFlavor);
  }

  public boolean isImage() {
    return dataFlavor.equals(DataFlavor.imageFlavor);
  }

  public void setStrData(String strData) {
    this.strData = strData;
  }

  public String getStrData() {
    return strData;
  }

  public void setDataFlavor(DataFlavor dataFlavor) {
    this.dataFlavor = dataFlavor;
  }

  public DataFlavor getDataFlavor() {
    return dataFlavor;
  }

  public void setBufferedImage(BufferedImage bufferedImage) {
    this.bufferedImage = bufferedImage;
  }

  public BufferedImage getBufferedImage() {
    return bufferedImage;
  }
}

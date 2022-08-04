package com.sundayli.fastcopywin.system;

import com.sundayli.fastcopywin.system.model.ClipboardChangeOperation;
import com.sundayli.fastcopywin.system.model.ClipboardData;
import com.sundayli.fastcopywin.system.model.ImageTransferable;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CustomClipboard {
  private static CustomClipboard customClipboard;
  private Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
  private List<ClipboardChangeOperation> clipboardChangeOperations = new ArrayList<>();

  private CustomClipboardOwner customClipboardOwner = new CustomClipboardOwner(clipboardChangeOperations);

  private CustomClipboard() {
    clipboard.setContents(clipboard.getContents(null), customClipboardOwner);
  }

  public static synchronized CustomClipboard getInstance() {
    if (customClipboard == null) {
      customClipboard = new CustomClipboard();
    }
    return customClipboard;
  }

  public synchronized void addListener(ClipboardChangeOperation clipboardChangeOperation) {
    clipboardChangeOperations.add(clipboardChangeOperation);
  }

  public void coverClipboardData(ClipboardData clipboardData) {
    if (clipboardData.isString()) {
      StringSelection stringSelection = new StringSelection(clipboardData.getStrData());
      clipboard.setContents(stringSelection, customClipboardOwner);
    } else if (clipboardData.isImage()) {
      ImageTransferable imageTransferable = new ImageTransferable(clipboardData.getBufferedImage());
      clipboard.setContents(imageTransferable, customClipboardOwner);
    }
  }

  static class CustomClipboardOwner implements ClipboardOwner {
    List<ClipboardChangeOperation> clipboardChangeOperations;
    CustomClipboardOwner(List<ClipboardChangeOperation> clipboardChangeOperations) {
      this.clipboardChangeOperations = clipboardChangeOperations;
    }

    @Override
    public void lostOwnership(Clipboard clipboard, Transferable contents) {
      // 立刻使用会报错 延迟一下 然后再更改剪切板 owner
      try {
        Thread.sleep(500);
      } catch (InterruptedException e) {// NOSONAR
        // NOSONAR
      }
      ClipboardData clipboardData = null;
      if (clipboard.isDataFlavorAvailable(DataFlavor.stringFlavor)){
        clipboardData = new ClipboardData();
        clipboardData.setStrData((String)getClipboardData(clipboard, DataFlavor.stringFlavor));
        clipboardData.setDataFlavor(DataFlavor.stringFlavor);
      } else if (clipboard.isDataFlavorAvailable(DataFlavor.imageFlavor)) {
        clipboardData = new ClipboardData();
        clipboardData.setBufferedImage((BufferedImage)getClipboardData(clipboard, DataFlavor.imageFlavor));
        clipboardData.setDataFlavor(DataFlavor.imageFlavor);
      }
      if (clipboardData != null) {
        for (ClipboardChangeOperation clipboardChangeOperation : clipboardChangeOperations) {
          clipboardChangeOperation.operation(clipboardData);
        }
      }
      clipboard.setContents(clipboard.getContents(null), this);
    }

    private Object getClipboardData(Clipboard clipboard, DataFlavor dataFlavor) {
      try {
        return clipboard.getData(dataFlavor);
      } catch (UnsupportedFlavorException | IOException e) {
        e.printStackTrace();
      }
      return null;
    }
  }


}

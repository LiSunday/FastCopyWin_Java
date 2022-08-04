package com.sundayli.fastcopywin.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import javafx.scene.image.Image;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelReader;
import javax.imageio.ImageIO;


public class ImageUtils {

  private ImageUtils() {}

  // ------------------------------ Image -------------------------------
  public static Image toImage(BufferedImage bufferedImage) {
    ByteArrayOutputStream os = new ByteArrayOutputStream();
    try {
      ImageIO.write(bufferedImage, "png", os);
    } catch (IOException e) {
      return null;
    }
    return new Image(new ByteArrayInputStream(os.toByteArray()));
  }

  public static Image toImage(String str) {
    return toImage(toBufferedImage(str));
  }

  // ------------------------------ String -------------------------------
  public static String toString(BufferedImage bufferedImage) {
    ByteArrayOutputStream os = new ByteArrayOutputStream();
    try {
      ImageIO.write(bufferedImage, "png", os);
    } catch (IOException e) {
      return null;
    }
    return Base64.getEncoder().encodeToString(os.toByteArray());
  }

  // ------------------------------ BufferedImage -------------------------------
  public static BufferedImage toBufferedImage(String str) {
    try {
      return ImageIO.read(new ByteArrayInputStream(Base64.getDecoder().decode(str)));
    } catch (IOException e) {
      return null;
    }
  }
  public static BufferedImage toBufferedImage(Image img) {
    PixelReader pr = img.getPixelReader();
    int type = getBestBufferedImageType(pr.getPixelFormat());
    int w = (int)img.getWidth();
    int h = (int)img.getHeight();
    BufferedImage bufferedImage = new BufferedImage(w, h, type);
    for (int i = 0; i < w; i++) {
      for (int j = 0; j < h; j++) {
        bufferedImage.setRGB(i, j, pr.getArgb(i, j));
      }
    }
    return bufferedImage;
  }

  private static int getBestBufferedImageType(PixelFormat<?> fxFormat)
  {
    switch (fxFormat.getType()) {
      default:
      case BYTE_BGRA_PRE:
      case INT_ARGB_PRE:
        return BufferedImage.TYPE_INT_ARGB_PRE;
      case BYTE_BGRA:
      case INT_ARGB:
        return BufferedImage.TYPE_INT_ARGB;
      case BYTE_RGB:
        return BufferedImage.TYPE_INT_RGB;
      case BYTE_INDEXED:
        return (fxFormat.isPremultiplied()
          ? BufferedImage.TYPE_INT_ARGB_PRE
          : BufferedImage.TYPE_INT_ARGB);
    }
  }
}

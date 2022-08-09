package com.sundayli.fastcopywin;

import java.io.FileInputStream;
import org.springframework.util.DigestUtils;

public class ClientConfig {
  private static ClientConfig clientConfig;

  private static String filePath = "";

  private static String fileMd5;

  private ClientConfig() {}

  public static synchronized ClientConfig getInstance() {
    try {
      String md5Str = DigestUtils.md5DigestAsHex(new FileInputStream(filePath));
      if (clientConfig != null && fileMd5.equals(md5Str)) {
        clientConfig = null;
        fileMd5 = md5Str;
      }
    } catch (Exception ignored) {}
    if (clientConfig == null) {
      clientConfig = new ClientConfig();
    }
    return clientConfig;
  }
}

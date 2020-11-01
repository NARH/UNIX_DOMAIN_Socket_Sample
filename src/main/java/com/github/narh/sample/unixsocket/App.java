package com.github.narh.sample.unixsocket;

import java.io.File;
import java.util.ResourceBundle;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {

  private static Logger logger                = LoggerFactory.getLogger(App.class);

  private static ResourceBundle resources     = ResourceBundle.getBundle("config");

  public static final String SOCKET_FILE_NAME = "socket.file";

  public static void main(String ...args) throws Exception {
    File socketFile = (StringUtils.contains(resources.getString(SOCKET_FILE_NAME), IOUtils.DIR_SEPARATOR))
        ? new File(resources.getString(SOCKET_FILE_NAME))
        : new File(System.getProperty("java.io.tmpdir") + IOUtils.DIR_SEPARATOR + resources.getString(SOCKET_FILE_NAME));
    Server server   = new Server(socketFile);
    server.startUp();
  }
}

package com.github.narh.sample.unixsocket;

import java.io.File;
import java.util.ResourceBundle;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.newsclub.net.unix.AFUNIXSocketAddress;

import com.github.narh.sample.unixsocket.server.BaseServer;
import com.github.narh.sample.unixsocket.server.EchoServer;

public class EchoApp {

  private static ResourceBundle resources     = ResourceBundle.getBundle("config");

  public static final String SOCKET_FILE_NAME = "socket.file";

  public static void main(String ...args) throws Exception {
    File socketFile = (StringUtils.contains(resources.getString(SOCKET_FILE_NAME), IOUtils.DIR_SEPARATOR))
        ? new File(resources.getString(SOCKET_FILE_NAME))
        : new File(System.getProperty("java.io.tmpdir") + IOUtils.DIR_SEPARATOR + resources.getString(SOCKET_FILE_NAME));

    BaseServer server   = new EchoServer(new AFUNIXSocketAddress(socketFile));
    server.start();
  }
}

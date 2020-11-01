package com.github.narh.sample.unixsocket;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.Socket;

import org.apache.commons.io.IOUtils;
import org.newsclub.net.unix.AFUNIXServerSocket;
import org.newsclub.net.unix.AFUNIXSocketAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * UNIX Domain Socker を利用したサーバを起動します。
 */
public class Server {

  /** ロガー */
  private static final Logger logger = LoggerFactory.getLogger(Server.class);

  /** UNIX Domain Socket File */
  private final File socketFile;

  /** Socket を LISTEN するサーバ */
  private final AFUNIXServerSocket server;

  /**
   * コンストラクタ
   * @param socketFile UNIX Domain Socket File
   * @throws IOException Unix Domain Socket のLISTENに失敗した場合
   */
  public Server(final File socketFile) throws IOException {
    this.socketFile = socketFile;
    this.server     = AFUNIXServerSocket.newInstance();
    this.server.bind(new AFUNIXSocketAddress(this.socketFile));
    if(logger.isInfoEnabled()) logger.info("ファイル[ {} ]を用いてサーバを起動します。", this.socketFile.getAbsoluteFile());
  }

  public void startUp() throws IOException {

    if(null == server) {
      if(logger.isErrorEnabled()) logger.error("Unable startup server. server is null.");
      return;
    }

    while(!Thread.interrupted()) {
      if(logger.isInfoEnabled()) logger.info("Server startup. waiting for connection.");
      Socket socket = server.accept();
      if(logger.isDebugEnabled()) logger.debug("Connected: {}", socket);

      Reader reader   = new InputStreamReader(socket.getInputStream());
      Writer writer   = new OutputStreamWriter(socket.getOutputStream());

      IOUtils.write("Connected.", writer);
      writer.flush();

      String inputStr = "";
      while(reader.ready()) {
        inputStr    += IOUtils.toString(reader);
      }
      if(logger.isInfoEnabled()) logger.info("Received String: {}", inputStr);

      String data     = execute(inputStr);

      IOUtils.write(data, writer);
      writer.flush();
      if(logger.isInfoEnabled()) logger.info("Responsed String: {}", data);

      IOUtils.closeQuietly(reader);
      IOUtils.closeQuietly(writer);
      IOUtils.closeQuietly(socket);
    }

    shutdown();
  }

  public void shutdown() {
    if(logger.isInfoEnabled()) logger.info("Server shutdown.");

    if(null != server) {
      IOUtils.closeQuietly(this.server);
    }

    if(socketFile.exists()) socketFile.delete();
  }

  public String execute(final String inputStr) {
    return inputStr.toUpperCase();
  }
}

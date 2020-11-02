package com.github.narh.sample.unixsocket.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;

public class EchoServer extends BaseServer {

  public EchoServer(ServerSocket serverSocket) {
    super(serverSocket);
  }
  public EchoServer(SocketAddress listenAddress) {
    super(listenAddress);
  }

  @Override
  protected void doServeSocket(Socket socket) throws IOException {
    int bufferSize  = socket.getReceiveBufferSize();
    byte[] buffer   = new byte[bufferSize];

    try (InputStream is = socket.getInputStream();
        OutputStream os = socket.getOutputStream()) {

      int read;
      while((read = is.read(buffer)) != -1) {
        os.write(buffer, 0, read);
        os.flush();
      }
      os.flush();
    }
  }

}

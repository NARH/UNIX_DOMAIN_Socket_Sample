package com.github.narh.sample.unixsocket.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.concurrent.Future;

import org.newsclub.net.unix.AFUNIXSocket;
import org.newsclub.net.unix.server.AFUNIXSocketServer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class BaseServer extends AFUNIXSocketServer {

  private static final String msg = "Creating server: {}"
                                  + "\n"   + "with the following configuration:"
                                  + "\n\t" + "- maxConcurrentConnections: {}"
                                  + "\n\t" + "- serverTimeout: {}"
                                  + "\n\t" + "- socketTimeout: {}"
                                  + "\n\t" + "- serverBusyTimeout: {}";

  public BaseServer(SocketAddress listenAddress) {
    super(listenAddress);
  }

  public BaseServer(ServerSocket serverSocket) {
    super(serverSocket);
  }

  private String millisToHumanReadable(int millis, String zeroValue) {
    if (millis == 0 && zeroValue != null)
      return "0 [ms] (" + zeroValue + ")";

    float secs = millis / 1000f;
    return ((secs - (int) secs) == 0)
        ? millis + " [ms] == " + (int) (secs) + "s"
        : millis + " [ms] == " + secs + "s";
  }

  @Override
  protected void onServerStarting() {
    if(log.isInfoEnabled())
      log.info(msg
          , getClass().getName()
          , getMaxConcurrentConnections()
          , millisToHumanReadable(getServerTimeout(), "none")
          , millisToHumanReadable(getSocketTimeout(), "none")
          , millisToHumanReadable(getServerBusyTimeout(), "none"));
  }

  @Override
  protected void onServerBound(SocketAddress address) {
    if(log.isInfoEnabled()) log.info("Created server -- bound to {}", address);
  }

  @Override
  protected void onServerBusy(long busySince) {
    if(log.isWarnEnabled()) log.warn("Server is busy");
  }

  @Override
  protected void onServerReady(int activeCount) {
    if(log.isInfoEnabled()) log.info("Active connections: {}; waiting for the next connection...", activeCount);
  }

  @Override
  protected void onServerStopped(ServerSocket theServerSocket) {
    if(log.isInfoEnabled()) log.info("Close server {}", theServerSocket);
  }

  @Override
  protected void onSubmitted(Socket socket, Future<?> submit) {
    if(log.isInfoEnabled()) log.info("Accepted: {}", socket);
  }

  @Override
  protected void onBeforeServingSocket(Socket socket) {
    if(log.isInfoEnabled()) log.info("Serving socket: {}", socket);
    if (log.isInfoEnabled() && socket instanceof AFUNIXSocket) {
      try {
        log.info("Client's credentials: {}", ((AFUNIXSocket) socket).getPeerCredentials());
      }
      catch (IOException e) {
        if(log.isErrorEnabled()) log.error(e.getLocalizedMessage(), e);
      }
    }
  }

  @Override
  protected void onServerShuttingDown() {
    if(log.isInfoEnabled()) log.info("Nothing going on for a long time, I better stop listening");
  }

  @Override
  protected void onSocketExceptionDuringAccept(SocketException e) {
    if(log.isErrorEnabled()) log.error("ERROR: {}", e.getMessage(), e);
  }

  @Override
  protected void onSocketExceptionAfterAccept(Socket socket, SocketException e) {
    if(log.isWarnEnabled()) log.warn("Closed (not executed): {}", socket, e);
  }

  @Override
  protected void onServingException(Socket socket, Exception e) {
    if(log.isWarnEnabled() && socket.isClosed()) {
      log.warn("The other end disconnected ({}): {}", e.getMessage(), socket);
      return;
    }

    if(log.isErrorEnabled()) {
      log.error("Exception thrown in {}, connected: {},{},{},{}.{}"
          , socket
          , socket.isConnected()
          , socket.isBound()
          , socket.isClosed()
          , socket.isInputShutdown()
          , socket.isOutputShutdown());
    }
  }

  @Override
  protected void onAfterServingSocket(Socket socket) {
    if(log.isInfoEnabled()) log.info("Closed: {}", socket);
  }

  @Override
  protected void onListenException(Exception e) {
    if(log.isErrorEnabled()) log.error("ERROR: {}", e.getMessage(), e);
  }
}

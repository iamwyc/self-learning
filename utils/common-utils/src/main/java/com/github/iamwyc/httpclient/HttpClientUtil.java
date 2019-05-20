package com.github.iamwyc.httpclient;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

/**
 * @author : iamwyc
 * @version : 1.0
 * @date : 2019/5/20 22:45
 * httpclient的连接池，支持高并发
 *
 * 依赖包如下：
 * <dependency>
 * <groupId>commons-io</groupId>
 * <artifactId>commons-io</artifactId>
 * <version>2.4</version>
 * </dependency>
 *
 * <dependency>
 * <groupId>org.apache.httpcomponents</groupId>
 * <artifactId>httpcore</artifactId>
 * <version>4.4.10</version>
 * </dependency>
 *
 * <dependency>
 * <groupId>org.apache.httpcomponents</groupId>
 * <artifactId>httpclient</artifactId>
 * <version>4.5.3</version>
 * </dependency>
 */
public class HttpClientUtil {

  private static ConnectionKeepAliveStrategy keepAliveStrategy;
  private static PoolingHttpClientConnectionManager connectionManager;
  private static IdleConnectionMonitorThread idleConnectionMonitorThread;

  static {
    keepAliveStrategy = new ConnectionKeepAliveStrategy() {
      public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
        //默认定义时长为60s。可以根据定义
        return 60 * 1000;
      }
    };
    connectionManager = new PoolingHttpClientConnectionManager();
    // 最大连接数
    connectionManager.setMaxTotal(500);
    // 路由最大连接数
    connectionManager.setDefaultMaxPerRoute(50);
    idleConnectionMonitorThread = new IdleConnectionMonitorThread(connectionManager);
    idleConnectionMonitorThread.start();
  }

  /**
   * 获取连接池里的httpClient
   */
  private static CloseableHttpClient getCloseableHttpClient() {
    return HttpClients.custom()
        .setConnectionManager(connectionManager)
        .setKeepAliveStrategy(keepAliveStrategy)
        .setDefaultRequestConfig(
            RequestConfig.custom().setStaleConnectionCheckEnabled(true).build())
        .build();
  }

  /**
   * 定时线程：关闭连接池过期的连接
   */
  private static class IdleConnectionMonitorThread extends Thread {

    private final HttpClientConnectionManager connMgr;
    private volatile boolean shutdown;

    public IdleConnectionMonitorThread(HttpClientConnectionManager connMgr) {
      super();
      this.connMgr = connMgr;
    }

    @Override
    public void run() {
      try {
        while (!shutdown) {
          synchronized (this) {
            wait(5000);
            connMgr.closeExpiredConnections();
            connMgr.closeIdleConnections(30, TimeUnit.SECONDS);
          }
        }
      } catch (InterruptedException ex) {
        // terminate
      }
    }

    public void shutdown() {
      shutdown = true;
      synchronized (this) {
        notifyAll();
      }
    }
  }

  /**
   * 关闭定时线程
   */
  public synchronized static void shutdown() {
    idleConnectionMonitorThread.shutdown();
  }

  /**
   * postJson数据
   *
   * @param url
   *     请求http地址
   * @param message
   *     请求的json数据
   * @param isDefaultClient
   *     是否使用默认的httpclient
   */
  public static String postJson(String url, String message, boolean isDefaultClient) {
    CloseableHttpClient client;
    if (isDefaultClient) {
      //使用默认的httpclient方式
      client = HttpClients.createDefault();
    } else {
      client = getCloseableHttpClient();
    }
    String s = postJson(url, message, client);
    if (isDefaultClient) {
      try {
        if (client != null) {
          client.close();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return s;
  }

  /**
   * 提交postJson请求
   */
  private static String postJson(String url, String message, CloseableHttpClient httpClient) {

    HttpPost httpPost = new HttpPost(url);
    CloseableHttpResponse response = null;
    try {
      httpPost.setHeader("Accept", "application/json;charset=UTF-8");
      httpPost.setHeader("Content-Type", "application/json");
      StringEntity stringEntity = new StringEntity(message);
      stringEntity.setContentType("application/json;charset=UTF-8");

      httpPost.setEntity(stringEntity);
      response = httpClient.execute(httpPost);
      HttpEntity entity = response.getEntity();
      if (entity != null) {
        return IOUtils.toString(entity.getContent());
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (response != null) {
        try {
          EntityUtils.consume(response.getEntity());
          response.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    return null;
  }
}

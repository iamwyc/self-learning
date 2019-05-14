package com.github.iamwyc.server;

import com.github.iamwyc.api.hello.HelloService;
import com.github.iamwyc.server.service.HelloServiceImpl;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.THsHaServer;
import org.apache.thrift.server.TNonblockingServer;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TServer.Args;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.server.TThreadedSelectorServer;
import org.apache.thrift.transport.TFastFramedTransport;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportException;

/**
 * @author : iamwyc
 * @version : 1.0
 * @date : 2019/5/14 23:00
 * 参考文章：https://www.jianshu.com/p/047f9a5385df
 */
public class Server {

  private static HelloService.Processor processor = new HelloService.Processor(
      new HelloServiceImpl());
  private static int port = 9090;

  /**
   * 协议层：
   * 数据传输方式（序列化）
   * 客户端、服务端需要保持一致
   * TBinaryProtocol：二进制格式；
   * TCompactProtocol：压缩格式；
   * TJSONProtocol：JSON格式；
   * TSimpleJSONProtocol：提供JSON只写协议, 生成的文件很容易通过脚本语言解析；
   * TDebugProtocol：使用易懂的可读的文本格式，以便于debug
   */
  private static TBinaryProtocol.Factory protocolFactory = new TBinaryProtocol.Factory();

  /**
   * 传输层
   * 定义数据传输方式，可以为TCP/IP传输，内存共享或者文件共享等）被用作运行时库。
   * 客户端、服务端需要保持一致
   * TSocket：阻塞式socker；
   * TFramedTransport：以frame为单位进行传输，非阻塞式服务中使用；
   * TFileTransport：以文件形式进行传输；
   * TMemoryTransport：将内存用于I/O，java实现时内部实际使用了简单的ByteArrayOutputStream；
   * TZlibTransport：使用zlib进行压缩， 与其他传输方式联合使用，当前无java实现；
   */
  private static TFastFramedTransport.Factory transportFacotry = new TFastFramedTransport.Factory();

  public static void main(String[] args) throws TTransportException {
//    newSimpleServer();
//    newThreadPoolServer();
//    newNonblockingServer();
//    newHsHaServer();
    newThreadedSelectorServer();
  }

  /**
   * 阻塞式
   * TSimpleServer 是一个单线程阻塞 I/O 的 Server，它循环监听新请求的到来并对请求进行处理
   * 但一次只能接收和处理一个Socket连接，效率低，主要用于测试和学习，实际开发很少用到
   */
  public static void newSimpleServer() throws TTransportException {
    TServerTransport serverTransport = new TServerSocket(port);
    TBinaryProtocol.Factory protocolFactory = new TBinaryProtocol.Factory();
    TServer server = new TSimpleServer(
        new Args(serverTransport).processor(processor).protocolFactory(protocolFactory)
            .transportFactory(transportFacotry));
    System.out.println("开启Thrift服务器，监听端口:" + port);
    server.serve();
  }

  /**
   * 阻塞式
   * TThreadPoolServer 模式采用阻塞 Socket 方式工作
   * 主线程负责阻塞监听是否有新的 Socket 连接，业务交由一个线程池进行处理
   * 该模式的处理能力受限于线程池的工作能力，当并发请求数大于线程池中的线程数时，新的请求会进入队列中排队等待处理
   */
  public static void newThreadPoolServer() throws TTransportException {
    TServerSocket serverSocket = new TServerSocket(port);
    TBinaryProtocol.Factory protocolFactory = new TBinaryProtocol.Factory();
    TThreadPoolServer.Args serverArgs = new TThreadPoolServer.Args(serverSocket)
        .processor(processor).transportFactory(transportFacotry)
        .protocolFactory(protocolFactory);
    TServer server = new TThreadPoolServer(serverArgs);
    System.out.println("开启Thrift服务器，监听端口:" + port);
    server.serve();
  }


  /**
   * 非阻塞式
   * NonblockingServer 是一个单线程 NIO 的 Server，NIO 通过 Selector 循环监听所有 Socket，每次 selector 结束时，处理所有就绪状态的
   * Socket 一个阻塞 I/O 线程一次只能处理一个 Socket 连接，而一个 NIO 线程可以处理多个 Socket 连接 虽然一个 NIO 线程可以同时接收多个 Socket
   * 连接，但在处理任务时仍然是阻塞的
   */
  public static void newNonblockingServer() throws TTransportException {
    TNonblockingServerSocket serverSocket = new TNonblockingServerSocket(port);
    TBinaryProtocol.Factory protocolFactory = new TBinaryProtocol.Factory();
    TNonblockingServer.Args serverArgs = new TNonblockingServer.Args(serverSocket)
        .processor(processor).transportFactory(transportFacotry)
        .protocolFactory(protocolFactory);
    TServer server = new TNonblockingServer(serverArgs);
    System.out.println("开启Thrift服务器，监听端口:" + port);
    server.serve();
  }

  /**
   * 非阻塞式
   * THsHaServer 是 TNonBlockingServer 的子类
   * 在 TNonBlockingServer 中,采用一个线程来完成对所有的 Socket监听和业务处理，造成了效率低下
   * 而 THsHaServer 模式使用了一个线程池来专门进行业务处理
   */
  public static void newHsHaServer() throws TTransportException {
    TNonblockingServerSocket serverTransport = new TNonblockingServerSocket(port);
    THsHaServer.Args serverArgs = new THsHaServer.Args(serverTransport)
        .processor(processor)
        .protocolFactory(protocolFactory)
        .transportFactory(transportFacotry);
    TServer server = new THsHaServer(serverArgs);
    System.out.println("开启Thrift服务器，监听端口:" + port);
    server.serve();
  }

  /**
   * 非阻塞式
   * ThreadedSelectorServer 是 Thrift 目前最高级的模式，它内部由几个部分构成
   * 1.一个 AcceptThread 线程对象，专门用于处理 Socket 上的新连接
   * 2.若干个 SelectorThread 对象专门用于处理业务 Socket 的网络 I/O 操作，所有网络数据的读写都是由这些线程来完成的
   * 3.一个负载均衡器 SelectorThreadLoadBalancer 对象，主要用于 AcceptThread 线程接收到新的 Socket 连接请求时，将请求分配给
   * SelectorThread 线程
   * 4.一个 ExecutorService 线程池，负责完成业务逻辑处理
   */
  public static void newThreadedSelectorServer() throws TTransportException {
    TNonblockingServerSocket serverSocket = new TNonblockingServerSocket(port);

    TThreadedSelectorServer.Args serverArgs = new TThreadedSelectorServer.Args(serverSocket)
        .processor(processor).transportFactory(transportFacotry)
        .protocolFactory(protocolFactory);
    TServer server = new TThreadedSelectorServer(serverArgs);
    System.out.println("开启Thrift服务器，监听端口:" + port);
    server.serve();
  }
}

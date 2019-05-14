# Thrift使用示例
## 一、下载thrift文件编译器：[地址][1]
[1]: http://thrift.apache.org/download
## 二、定义服务thrift文件:
src\main\resources\demo.thrift
```
namespace java com.github.iamwyc.api.hello

enum RequestType {
  JustForFun = 1,
  CodeOnly = 2,
  HappyRun = 3
}

struct HelloResponse {
  1: string welcomeString
  2: i32 serviceId
  3: list<string> gifts
}

struct HelloRequest {
  1: string name
  2: i64 timestamp
}

service HelloService {
  HelloResponse sayHello(1: RequestType type,2: HelloRequest request)
}
```
## 三、生成Java文件
```
thrift -r -gen java demo.thrift
```
## 四、服务端代码
### 1. HelloService的实现
```java
public class HelloServiceImpl implements HelloService.Iface {

  public HelloResponse sayHello(RequestType type, HelloRequest request) throws TException {
    HelloResponse helloResponse = new HelloResponse();
    helloResponse.serviceId= (int) System.currentTimeMillis();
    helloResponse.welcomeString="调用成功："+type.name()+","+request.getName();
    return helloResponse;
  }
}
```
### 2. 启动服务
这里只展示了阻塞式传输层,具体其他的方式查看源码级源码注释：
com.github.iamwyc.server.Server.java
```java
public class Server {

  private static HelloService.Processor processor = new HelloService.Processor(
      new HelloServiceImpl());
  private static int port = 9090;
  /**
   * 数据协议
   * 客户端、服务端需要保持一致
   */
  private static TBinaryProtocol.Factory protocolFactory = new TBinaryProtocol.Factory();

  /**
   * 传输层
   * 客户端、服务端需要保持一致
   */
  private static TFastFramedTransport.Factory transportFacotry = new TFastFramedTransport.Factory();

  public static void main(String[] args) throws TTransportException {
    newSimpleServer();
  }
  
  public static void newSimpleServer() throws TTransportException {
    TServerTransport serverTransport = new TServerSocket(port);
    TBinaryProtocol.Factory protocolFactory = new TBinaryProtocol.Factory();
    TServer server = new TSimpleServer(
        new Args(serverTransport).processor(processor).protocolFactory(protocolFactory)
            .transportFactory(transportFacotry));
    System.out.println("开启Thrift服务器，监听端口:" + port);
    server.serve();
  }
}

```
## 四、客户端代码
```java
 public static void main(String[] args) throws TException {
    newSimpleConsumer();
  }

  public static void newSimpleConsumer() throws TException {
    TTransport transport = new TSocket("localhost", 9090);
    transport.open();
    TFastFramedTransport tFastFramedTransport = new TFastFramedTransport(transport);
    TBinaryProtocol protocol = new TBinaryProtocol(tFastFramedTransport);


    HelloService.Client client = new HelloService.Client(protocol);
    HelloRequest req=new HelloRequest();
    req.setTimestamp(System.currentTimeMillis());
    req.setName("iamwyc");
    HelloResponse helloResponse = client.sayHello(RequestType.CodeOnly, req);
    System.out.println(helloResponse.getWelcomeString());
  }
```

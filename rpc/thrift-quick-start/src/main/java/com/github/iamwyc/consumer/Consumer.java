package com.github.iamwyc.consumer;

import com.github.iamwyc.api.hello.HelloRequest;
import com.github.iamwyc.api.hello.HelloResponse;
import com.github.iamwyc.api.hello.HelloService;
import com.github.iamwyc.api.hello.RequestType;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.transport.TFastFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

/**
 * @author : iamwyc
 * @version : 1.0
 * @date : 2019/5/14 23:00
 */
public class Consumer {

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
}

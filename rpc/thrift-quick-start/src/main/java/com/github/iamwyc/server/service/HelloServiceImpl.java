package com.github.iamwyc.server.service;

import com.github.iamwyc.api.hello.HelloRequest;
import com.github.iamwyc.api.hello.HelloResponse;
import com.github.iamwyc.api.hello.HelloService;
import com.github.iamwyc.api.hello.RequestType;
import org.apache.thrift.TException;

/**
 * @author : iamwyc
 * @version : 1.0
 * @date : 2019/5/14 23:04
 */
public class HelloServiceImpl implements HelloService.Iface {

  public HelloResponse sayHello(RequestType type, HelloRequest request) throws TException {
    HelloResponse helloResponse = new HelloResponse();
    helloResponse.serviceId= (int) System.currentTimeMillis();
    helloResponse.welcomeString="调用成功："+type.name()+","+request.getName();
    return helloResponse;
  }

}

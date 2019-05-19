package com.github.iamwyc.real.service;

import com.github.iamwyc.api.DemoService;
import com.github.iamwyc.real.api.HelloService;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;

/**
 * @author : iamwyc
 * @version : 1.0
 * @date : 2019/5/19 0:04
 */
//@Service(interfaceClass = HelloService.class, registry = "real", version = "0.0.1",dynamic = true)
@org.springframework.stereotype.Service
public class HelloServiceImpl implements HelloService {

  @Reference(interfaceClass= DemoService.class,registry = "mid")
  private DemoService demoService;

  @Override
  public String sayHello(String name, int age) {
    String welcome = demoService.sayName(name);
    return welcome + " - " + "HelloServiceImpl -" + age;
  }
}

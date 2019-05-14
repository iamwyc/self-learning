package com.github.iamwyc.consumer.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.iamwyc.api.DemoService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author : iamwyc
 * @version : 1.0
 * @date : 2019/5/14 21:41
 */
@Component
public class DemoConsumerService {

  @Reference(check = false)
  private DemoService demoService;
//  @Value("${spring.dubbo.protocol.serialization}")
  String serialization;

  public void say(){
    String d = demoService.sayName("DemoConsumerService");
    System.out.println("------------------------------");
    System.out.println(d);
    System.out.println(serialization);
    System.out.println("------------------------------");
  }
}

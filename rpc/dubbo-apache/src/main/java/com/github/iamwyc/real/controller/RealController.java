package com.github.iamwyc.real.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.iamwyc.api.DemoService;
import com.github.iamwyc.real.api.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : iamwyc
 * @version : 1.0
 * @date : 2019/5/19 0:02
 */
@RestController
@RequestMapping("/real")
public class RealController {

  @Autowired
  private HelloService helloService;
  @RequestMapping("")
  public String real(){

    return helloService.sayHello("iamwyc",18);
  }
}

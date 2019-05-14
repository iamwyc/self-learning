package com.github.iamwyc.consumer;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.spring.boot.annotation.EnableDubboConfiguration;
import com.github.iamwyc.api.DemoService;
import com.github.iamwyc.consumer.service.DemoConsumerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubboConfiguration
public class Consumer implements CommandLineRunner {

  @Autowired
  private DemoConsumerService demoConsumerService;

  public static void main(String[] args) {
    SpringApplication.run(Consumer.class, args);
  }

  @Override
  public void run(String... args) throws Exception {
    demoConsumerService.say();
  }
}

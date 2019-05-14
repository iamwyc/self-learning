package com.github.iamwyc.provider;

import com.alibaba.dubbo.spring.boot.annotation.EnableDubboConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubboConfiguration
public class Provider implements CommandLineRunner {

  @Value("${spring.dubbo.application.name}")
  private String value;

  public static void main(String[] args) {
    SpringApplication.run(Provider.class, args);
  }

  @Override
  public void run(String... args) throws Exception {

    System.out.println("启动成功" + value);
  }
}

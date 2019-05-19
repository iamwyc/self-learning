package com.github.iamwyc;

import java.util.Arrays;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.PropertySource;

/**
 * @author : iamwyc
 * @version : 1.0
 * @date : 2019/5/14 22:33
 */
@SpringBootApplication
@EnableDubbo(scanBasePackages = "com.github.iamwyc.real.service")
@PropertySource("classpath:application-dubbo.properties")
public class RealApplication extends SpringBootServletInitializer {

  public static void main(String[] args) {
//    new SpringApplicationBuilder(RealApplication.class).
//        run(args);

    SpringApplication.run(RealApplication.class, args);
  }
  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
    return builder.sources(RealApplication.class);
  }
}


package iamwyc.server;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

public class Provider {
    public static void main(String[] args) throws IOException {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath*:provider.xml");
        context.start();
        System.out.println("服务已经启动...");
        System.in.read();
    }
}

package iamwyc.server;

import iamwyc.api.DemoService;

public class DemoServiceImpl implements DemoService {
    @Override
    public String sayHello(String name) {
        return "hello "+name;
    }
}

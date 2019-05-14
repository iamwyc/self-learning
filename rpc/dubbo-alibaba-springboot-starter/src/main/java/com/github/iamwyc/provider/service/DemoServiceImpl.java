package com.github.iamwyc.provider.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.iamwyc.api.DemoService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Service(interfaceClass = DemoService.class,protocol = {"fastjson","original"})
@Component
public class DemoServiceImpl implements DemoService {
//    @Value(value = "${spring.dubbo.protocol.serialization}")
    String serialization;
    @Override
    public String sayName(String name) {
        return "from provider2: hello " + name + "! I am provider two >"+serialization;
    }
}

# dubbo-demo

## 一、接口定义：DemoService.java
```
package iamwyc.api;

public interface DemoService {
    String sayHello(String name);
}
```
## 二、生产者
### 1. 实现接口

```
package iamwyc.server;

import iamwyc.api.DemoService;

public class DemoServiceImpl implements DemoService {
    @Override
    public String sayHello(String name) {
        return "hello "+name;
    }
}
```
### 2.pom.xml

```
<dependencies>
    <!--注意这里还需要引入接口的依赖，因为本测试是demo这里就省略了-->
    <!-- spring begin -->
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-context</artifactId>
        <version>4.3.14.RELEASE</version>
    </dependency>
    <!-- spring end -->

    <!-- dubbo begin -->
    <dependency>
        <groupId>com.alibaba</groupId>
        <artifactId>dubbo</artifactId>
        <version>2.6.4</version>
    </dependency>
    <!-- dubbo end -->

    <!-- 注册中心zookeeper begin -->
    <dependency>
        <groupId>org.apache.zookeeper</groupId>
        <artifactId>zookeeper</artifactId>
        <version>3.4.6</version>
    </dependency>
    <!--注册中心zookeeper end-->

    <!-- log begin -->
    <dependency>
        <groupId>commons-logging</groupId>
        <artifactId>commons-logging</artifactId>
        <version>1.1.1</version>
    </dependency>
    <dependency>
        <groupId>log4j</groupId>
        <artifactId>log4j</artifactId>
        <version>1.2.17</version>
        <exclusions>
            <exclusion>
                <groupId>com.sun.jdmk</groupId>
                <artifactId>jmxtools</artifactId>
            </exclusion>
            <exclusion>
                <groupId>com.sun.jmx</groupId>
                <artifactId>jmxri</artifactId>
            </exclusion>
            <exclusion>
                <artifactId>jms</artifactId>
                <groupId>javax.jms</groupId>
            </exclusion>
            <exclusion>
                <artifactId>mail</artifactId>
                <groupId>javax.mail</groupId>
            </exclusion>
        </exclusions>
    </dependency>
    <!-- log end -->

    <!-- other begin -->
    <dependency>
        <groupId>io.netty</groupId>
        <artifactId>netty-all</artifactId>
        <version>4.1.35.Final</version>
    </dependency>
    <dependency>
        <groupId>com.101tec</groupId>
        <artifactId>zkclient</artifactId>
        <version>0.10</version>
    </dependency>
    <dependency>
        <groupId>org.apache.curator</groupId>
        <artifactId>curator-framework</artifactId>
        <version>4.0.1</version>
    </dependency>
    <!-- other end -->
</dependencies>
```
### 3.provider.xml配置
```
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:dubbo="http://code.alibabatech.com/schema/dubbo"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
http://www.springframework.org/schema/beans/spring-beans.xsd
http://code.alibabatech.com/schema/dubbo
http://code.alibabatech.com/schema/dubbo/dubbo.xsd">

    <!-- 应用配置,用于配置当前应用应用信息,不管提供者还是消费者 -->
    <!-- name：必填(服务治理) ** 当前应用名称,用于注册中心计算应用间依赖关系,消费者和提供者应用名最好不要一样,此项不是匹配条件 -->
    <!-- organization：可选(服务治理) ** 组织名称,BU或者部门,建议写死 -->
    <!-- version：可选 (服务治理) ** 当前版本 -->
    <!-- owner：可选(服务治理) ** 应用负责人 ,填写负责人公司邮箱前缀 -->
    <!-- environment ：可选(服务治理) ** 应应用环境，如：develop/test/product，不同环境使用不同的缺省值，以及作为只用于开发测试功能的限制条件 -->
    <dubbo:application environment="develop" owner="wyc"
                       version="0.0.1" name="demo" organization="iamwyc"/>


    <!-- 使用zookeeper注册中心暴露服务地址 -->
    <!-- address：必填(服务发现) ** 注册中心服务器地址，如果地址没有端口缺省为9090，同一集群内的多个地址用逗号分隔，如：ip:port,ip:port，不同集群的注册中心，请配置多个<dubbo:registry>标签 -->
    <dubbo:registry address="zookeeper://127.0.0.1:2181"/>


    <!-- 监控中心配置 -->
    <!-- protocol：可选 (服务治理) ** 监控中心协议，如果为protocol="registry"，表示从注册中心发现监控中心地址，否则直连监控中心。 -->
    <dubbo:monitor protocol="dubbo"/>

    <!-- 指定服务的实现类-->
    <dubbo:service interface="iamwyc.api.DemoService" protocol="dubbo" ref="demoService"/>

    <!-- 服务的实现类-->
    <bean id="demoService" class="iamwyc.server.DemoServiceImpl"/>
</beans>
```
### 4. 使用
```
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
```

## 三、消费者
### 1.pom.xml（同生产者）
### 2. consumer.xml配置文件
```
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:dubbo="http://code.alibabatech.com/schema/dubbo"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
http://www.springframework.org/schema/beans/spring-beans.xsd
http://code.alibabatech.com/schema/dubbo
http://code.alibabatech.com/schema/dubbo/dubbo.xsd">

    <!-- 应用配置,用于配置当前应用应用信息,不管提供者还是消费者 -->
    <!-- name：必填(服务治理) ** 当前应用名称,用于注册中心计算应用间依赖关系,消费者和提供者应用名最好不要一样,此项不是匹配条件 -->
    <!-- organization：可选(服务治理) ** 组织名称,BU或者部门,建议写死 -->
    <!-- version：可选 (服务治理) ** 当前版本 -->
    <!-- owner：可选(服务治理) ** 应用负责人 ,填写负责人公司邮箱前缀 -->
    <!-- environment ：可选(服务治理) ** 应应用环境，如：develop/test/product，不同环境使用不同的缺省值，以及作为只用于开发测试功能的限制条件 -->

    <!--
    qos=Quality of Service，qos是Dubbo的在线运维命令，可以对服务进行动态的配置、控制及查询
    默认使用22222端口。因为本次测试consumer和provider在同一个服务上，为了避免冲突改用33333端口。
    冲突也不影响正常使用
    -->
    <dubbo:application environment="develop" owner="wyc"
                       version="0.0.1" name="demo" organization="iamwyc">
        <dubbo:parameter key="qos.enable" value="true"/>
        <dubbo:parameter key="qos.accept.foreign.ip" value="false"/>
        <dubbo:parameter key="qos.port" value="33333"/>
    </dubbo:application>


    <!-- 使用zookeeper注册中心暴露服务地址 -->
    <!-- address：必填(服务发现) ** 注册中心服务器地址，如果地址没有端口缺省为9090，同一集群内的多个地址用逗号分隔，如：ip:port,ip:port，不同集群的注册中心，请配置多个<dubbo:registry>标签 -->
    <dubbo:registry address="zookeeper://127.0.0.1:2181"/>

    <!-- address：必填(服务发现) ** 注册中心服务器地址，如果地址没有端口缺省为9090，同一集群内的多个地址用逗号分隔，如：ip:port,ip:port，不同集群的注册中心，请配置多个<dubbo:registry>标签 -->
    <!-- 多配注册中心配置文件 开始:-->
    
    <!-- xml方式 开始：-->
    <!-- <dubbo:registry id="ZK2181" address="zookeeper://127.0.0.1:2181"/> -->
    <!-- <dubbo:registry id="ZK2888" address="zookeeper://127.0.0.1:2888"/> -->
    <!--在使用dubbo:reference和dubbo:service 标签时添加registry参数。值为所需要使用的registry的id-->
    <!-- xml方式 结束：-->
        
    <!-- 注解方式 开始：-->
    <!-- 在配置文件(xml、properties、config类)中给不同的注册中心添加id -->
    <!-- 在使用@Reference和@Service 注解添加registry参数。值为所需要使用的registry的id -->
    <!-- 注解方式 结束：-->
    <!-- 多配注册中心配置文件 结束:-->
    
    <!-- 监控中心配置 -->
    <!-- protocol：可选 (服务治理) ** 监控中心协议，如果为protocol="registry"，表示从注册中心发现监控中心地址，否则直连监控中心。 -->
    <dubbo:monitor protocol="dubbo"/>

    <!--引入需要使用的接口-->
    <dubbo:reference id="demoService"
                     interface="iamwyc.api.DemoService"
                     protocol="dubbo"/>
</beans>
```
### 3.使用
```
package iamwyc.client;

import iamwyc.api.DemoService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Consumer {
    public static void main(String[] args) {
        //指定consumer.xml配置文件的路径
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
                new String[]{"consumer.xml"});
        context.start();        
        //获取配置文件引入的接口
        DemoService service = (DemoService) context.getBean("demoService");
        //调用接口
        System.out.println(service.sayHello("world"));
        context.close();
    }
}
```
## 四、其他说明
### 1.整个demo的文件结构


### 2.log4j.properties日志文件
```
log4j.rootCategory=info,CONSOLE
#测试用的配置文件
log4j.logger.CONSOLEBROWSE=info,CONSOLE
log4j.appender.CONSOLE=org.apache.log4j.ConsoleAppender
log4j.appender.CONSOLE.layout=org.apache.log4j.PatternLayout
log4j.appender.CONSOLE.layout.ConversionPattern=%d{yyyy-MM-dd HH:mm:ss,SSS} [%p] [%t] %c{1}: (%m)%n
```

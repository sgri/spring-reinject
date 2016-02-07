Re-inject
========

Inject mocks into Spring application context easily

Licensed under [Apache 2 License](http://www.apache.org/licenses/LICENSE-2.0.html)

## The goal
The project's goal is to make it possible to override Spring bean definitions with mocks when executing tests without modification of Spring context files or Java-based configuration classes.

Spring Re-inject is not designed for contexts when all  objects are mocks. It is not designed to be used in production context, too. It is designed to make it possible to override specific beans programatically without forcing you to change configuration files (or Java-based configuration classes) every time you need to change a  bean's implementation in test environment. 
 
## How it works
You add a special bean factory post-processor to your application context. Then you register your mocks in your test's constructor. Then Spring constructs an application context for your test case, and the post-processor replaces bean definitions with new bean definitions which refer to mocks. Original beans are never created. 

Your mock becomes a Spring bean, it means that it can have auto-wired fields like a normal bean!


## Add a Maven dependency
```
<repositories>
        <repository>
            <id>reinject</id>
            <name>reinject</name>
            <url>https://github.com/sgri/spring-reinject/raw/master/release</url>
            <layout>default</layout>
            <snapshots>
                <enabled>true</enabled>
            </snapshots>
            <releases>
                <updatePolicy>always</updatePolicy>
            </releases>
        </repository>
</repositories>
<dependencies>
   <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-reinject</artifactId>
        <version>0.3</version>
        <scope>test</scope>
        <exclusions>
                <exclusion>
                    <groupId>org.springframework</groupId>
                    <artifactId>spring-core</artifactId>
                </exclusion>
                <exclusion>
                    <groupId>org.springframework</groupId>
                    <artifactId>spring-context</artifactId>
                </exclusion>
       </exclusions>
  </dependency>
</dependencies>
```
## Add `ReInjectPostProcessor` to your Application context
### For XML files
_reinject-context.xml_
```
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
           http://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean class="org.springframework.reinject.ReInjectPostProcessor"/>
</beans>
```

And your JUnit test may look like:
```
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/org/springframework/reinject/reinject-context.xml", ...your XML files})
public class XmlConfigTest  {

```

### For Java-based configuration
```
@Configuration
public class ReInjectContext {
    @Bean
    public ReInjectPostProcessor mockInjectionPostProcessor() {
        return new ReInjectPostProcessor();
    }
}
```

You JUnit test may look like
```
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ReInjectContext.class, ... your classes})
public class Test  {
```
## Register your mocks

You need to register mocks before Spring starts, the right place to do it is  your test's constructor.
Let's suppose you want to substitute a bean named "service" witch a mock.

### You can substitute a bean's implementation class
```
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ReInjectContext.class, ... your classes})
class MyTest {
  public MyTest() {
       ReInjectPostProcessor.inject("service", ServiceMock.class);
   }
```
### You can provide a pre-instantiated implementation
```
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ReInjectContext.class, ... your classes})
class MyTest {
  public MyTest() {
       ReInjectPostProcessor.inject("service",Service.class, new ServiceImpl() {
            @Override
            public String hello() {
                return "goodbye!";
            }
        });
   }
```

### Using EasyMock is possible
You can use  whatever library you like to deal with mocks, for example, EasyMock.
```
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ReInjectContext.class, ... your classes})
class MyTest {
  public MyTest() {
        IMocksControl niceControl = EasyMock.createNiceControl();
        Service mock = niceControl.createMock(Service.class);
        EasyMock.expect(mock.hello()).andReturn("easyMock");
        niceControl.replay();
        ReInjectPostProcessor.inject("service", mock);
    }
```
## Code samples
Code samples can be found [here](https://github.com/sgri/spring-reinject/tree/master/src/test/java/org/springframework/reinject)

package org.springframework.reinject;


import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.AssertJUnit;
import org.testng.annotations.Test;

/**
 * @author Sergey Grigoriev
 */
@ContextConfiguration(classes = {ReInjectContext.class, ProviderTest.ProviderTestContext.class})
public class ProviderTest extends AbstractTestNGSpringContextTests {

    @Inject @Named("service1") private Service service1;
    @Inject @Named("service2") private Service service2;

    public ProviderTest() {
        ReInjectPostProcessor.inject("service1",Service.class, new ServiceMock() {
            @Override
            public String hello() {
                return "hello1";
            }
        });
        ReInjectPostProcessor.inject("service2",Service.class, new ServiceMock() {
            @Override
            public String hello() {
                return "hello2";
            }
        });
    }

    @Test
    public void overrideProvider() {
        AssertJUnit.assertEquals("hello1", service1.hello());
        AssertJUnit.assertEquals("hello2", service2.hello());
    }

    static class ProviderTestContext {
        @Bean
        public FactoryBean<Service>  service1() {
            FactoryBean<Service> factoryBean = new FactoryBean<Service>() {
                @Override
                public Service getObject() throws Exception {
                    return new ServiceImpl();
                }

                @Override
                public Class<?> getObjectType() {
                    return Service.class;
                }

                @Override
                public boolean isSingleton() {
                    return true;
                }
            };
            return factoryBean;
        }
        @Bean
        public Service  service2() {
            return new ServiceImpl();
        }
    }

}
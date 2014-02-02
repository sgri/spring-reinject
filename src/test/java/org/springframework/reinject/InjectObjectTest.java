package org.springframework.reinject;


import static org.junit.Assert.assertEquals;

import javax.inject.Inject;
import javax.inject.Named;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;

/**
 * @author Sergey Grigoriev
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ReInjectContext.class, InjectObjectTest.ProviderTestContext.class})
public class InjectObjectTest  {

    @Inject @Named("service1") private Service service1;
    @Inject @Named("service2") private Service service2;

    public InjectObjectTest() {
        ReInjectPostProcessor.inject("service1",Service.class, new ServiceMock() {
            @Override
            public String hello() {
                return "hello1";
            }
        });
        ReInjectPostProcessor.inject("service2", new ServiceMock() {
            @Override
            public String hello() {
                return "hello2";
            }
        });
    }

    @Test
    public void overrideProvider() {
        assertEquals("hello1", service1.hello());
        assertEquals("hello2", service2.hello());
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
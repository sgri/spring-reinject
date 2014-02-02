package org.springframework.reinject;


import javax.inject.Inject;
import javax.inject.Provider;

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

    @Inject private Service service;

    public ProviderTest() {
        MockInjectionPostProcessor.inject("service", new ServiceMock());
    }

    @Test
    public void overrideProvider() {
        AssertJUnit.assertEquals("goodbye!", service.hello());
    }

    static class ProviderTestContext {
        @Bean
        public FactoryBean<Service>  service() {
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


    }
}
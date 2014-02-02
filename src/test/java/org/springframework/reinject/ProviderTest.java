package org.springframework.reinject;


import javax.inject.Inject;
import javax.inject.Provider;

import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.AssertJUnit;
import org.testng.annotations.Test;

/**
 * @author Sergey Grigoriev
 */
@ContextConfiguration(classes = {ProviderTest.ProviderTestContext.class})
public class ProviderTest extends AbstractTestNGSpringContextTests {

    @Inject private Service service;

    public ProviderTest() {
        MockInjectionPostProcessor.inject("service", new Provider<Object>() {
            @Override
            public Object get() {
                return new ServiceMock();
            }
        });
    }

    @Test
    public void overrideProvider() {
        AssertJUnit.assertEquals("ss", service.hello());
    }

    static class ProviderTestContext {
        @Bean
        public Service service() {
            return new ServiceImpl();
        }
    }
}
}

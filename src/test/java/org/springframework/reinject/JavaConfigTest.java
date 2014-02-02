package org.springframework.reinject;

import static org.testng.AssertJUnit.assertEquals;

import javax.inject.Inject;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

/**
 * @author Sergey Grigoriev
 */
@ContextConfiguration(classes = {ReInjectContext.class, JavaConfigTest.AppContext.class})
public class JavaConfigTest extends AbstractTestNGSpringContextTests {
    @Inject private Service service;
    public JavaConfigTest() {
        MockInjectionPostProcessor.inject("service", ServiceMock.class);
    }

    @Test
    public void inject() {
        assertEquals("goodbye!", service.hello());
    }

    @Configuration
    static class AppContext {

        @Bean public Object dependent() {
            assertEquals("goodbye!", service().hello());
            return new Object();
        }

        @Bean
        public Service service() {
            return new ServiceImpl();
        }

    }
}

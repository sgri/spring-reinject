package org.springmock;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

/**
 * @author Sergey Grigoriev
 */
@ContextConfiguration(classes = {JavaConfigTest.AppContext.class})
public class JavaConfigTest extends AbstractTestNGSpringContextTests {
    @Test
    public void inject() {

    }

    @Configuration
    static class AppContext {
        @Bean
        public MockInjectionPostProcessor mockInjectionPostProcessor() {
             return new MockInjectionPostProcessor();
        }

        @Bean
        public Service service() {
            return new ServiceImpl();
        }

    }
}

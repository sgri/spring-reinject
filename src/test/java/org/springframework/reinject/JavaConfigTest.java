package org.springframework.reinject;

import static org.junit.Assert.assertEquals;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;

/**
 * @author Sergey Grigoriev
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ReInjectContext.class, JavaConfigTest.AppContext.class})
public class JavaConfigTest  {
    @Inject private Service service;
    public JavaConfigTest() {
        ReInjectPostProcessor.inject("service", ServiceMock.class);
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

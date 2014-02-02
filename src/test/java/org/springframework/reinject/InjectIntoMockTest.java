package org.springframework.reinject;

import javax.inject.Inject;

import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Sergey Grigoriev
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ReInjectContext.class, InjectIntoMockTest.InjectIntoMockTestContext.class})
public class InjectIntoMockTest {
    @Inject private Printer printer;

    public InjectIntoMockTest() {
        ReInjectPostProcessor.inject("printer", new Printer(){
            @Inject private Service service;
            @Override
            public String print() {
                Assert.assertNotNull(service);
                return super.print();
            }
        });
    }

    @Test
    public void injectIntoMock() {
        printer.print();
    }

    @Configuration
    static class InjectIntoMockTestContext {
        @Bean
        public  Service service() {
            return new ServiceImpl();
        }

        @Bean
        public Printer printer() {
            return new Printer();
        }

    }
}

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
@ContextConfiguration(classes = ReInjectTest.Context.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class ReInjectTest {


    @Inject private Printer printer;

    public ReInjectTest() {
        ReInjectPostProcessor.inject("hello", new SayHello() {
            @Override
            public String hello() {
                return "goodbye!";
            }
        });
    }

    @Test
    public void hello() {
        Assert.assertEquals("goodbye!", printer.print());
    }

    @Configuration
    static class Context {
        @Bean public SayHello hello() {
            return new SayHello() {
                @Override
                public String hello() {
                    return "hello";
                }
            };
        }

        @Bean public ReInjectPostProcessor reInjectPostProcessor() {
            return new ReInjectPostProcessor();
        }

        @Bean
        public Printer printer() {
            return new Printer();
        }
    }

    static interface SayHello {
        String hello();
    }

    static class Printer {
        @Inject SayHello sayHello;
        public String print() {
            return sayHello.hello();
        }
    }
}

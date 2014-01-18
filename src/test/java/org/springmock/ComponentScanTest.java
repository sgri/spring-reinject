package org.springmock;

import javax.inject.Inject;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.AssertJUnit;
import org.testng.annotations.Test;

/**
 * @author Sergey Grigoriev
 */
@ContextConfiguration(classes = {ComponentScanTest.AppContext.class})
public class ComponentScanTest extends AbstractTestNGSpringContextTests{
    @Inject private Service service;

    public ComponentScanTest() {
        MockInjectionPostProcessor.injectBean("serviceImpl", MockedService1.class);
    }

    @Test
    public void injection() {
        AssertJUnit.assertEquals("mock1", service.hello());
    }

    @Configuration
    @ComponentScan(basePackages = "org.springmock")
    static class AppContext {

    }
}
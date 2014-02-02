package org.springframework.reinject;

import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired private Service service;

    public ComponentScanTest() {
        ReInjectPostProcessor.inject("service", ServiceMock.class);
    }

    @Test
    public void injection() {
        AssertJUnit.assertEquals("goodbye!", service.hello());
    }

    @Configuration
    @ComponentScan(basePackages = "org.springframework.reinject")
    static class AppContext {

    }
}
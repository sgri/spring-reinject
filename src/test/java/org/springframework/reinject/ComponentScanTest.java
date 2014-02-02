package org.springframework.reinject;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;

/**
 * @author Sergey Grigoriev
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ComponentScanTest.AppContext.class})
public class ComponentScanTest {
    @Autowired private Service service;

    public ComponentScanTest() {
        ReInjectPostProcessor.inject("service", ServiceMock.class);
    }

    @Test
    public void injection() {
        assertEquals("goodbye!", service.hello());
    }

    @Configuration
    @ComponentScan(basePackages = "org.springframework.reinject")
    static class AppContext {

    }
}
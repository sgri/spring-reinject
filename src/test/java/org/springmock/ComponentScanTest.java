package org.springmock;

import javax.inject.Inject;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.AssertJUnit;
import org.testng.annotations.Test;

/**
 * @author Sergey Grigoriev
 */
@ContextConfiguration(classes = {MockedServicesConfiguration.class})
public class ComponentScanTest extends AbstractTestNGSpringContextTests{
    @Inject private Service service;

    public ComponentScanTest() {
        MockInjectionPostProcessor.injectBean("serviceImpl", MockedService1.class);
    }

    @Test
    public void injection() {
        AssertJUnit.assertEquals("mock1", service.hello());
    }
}
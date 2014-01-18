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
public class MockInjectionPostProcessorTest extends AbstractTestNGSpringContextTests{
    @Inject private Service service;

    public MockInjectionPostProcessorTest() {
        MockInjectionPostProcessor.injectBean("service", MockedService.class);
    }

    @Test
    public void injection() {
        AssertJUnit.assertEquals("mocked", service.hello());
    }
}
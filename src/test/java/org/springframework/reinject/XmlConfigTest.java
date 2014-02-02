package org.springframework.reinject;

import javax.inject.Inject;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.AssertJUnit;
import org.testng.annotations.Test;

/**
 * @author Sergey Grigoriev
 */

@ContextConfiguration(locations = "classpath:/org/springframework/reinject/app-context.xml")
public class XmlConfigTest extends AbstractTestNGSpringContextTests {
    @Inject private Printer printer;

    public XmlConfigTest() {
        IMocksControl niceControl = EasyMock.createNiceControl();
        Service mock = niceControl.createMock(Service.class);
        EasyMock.expect(mock.hello()).andReturn("easyMock");
        niceControl.replay();
        ReInjectPostProcessor.inject("service", mock);
    }

    @Test
    public void inject() {
        AssertJUnit.assertEquals("easyMock", printer.print());
    }

}

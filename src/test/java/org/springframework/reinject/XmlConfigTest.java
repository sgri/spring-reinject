package org.springframework.reinject;

import static org.junit.Assert.assertEquals;

import javax.inject.Inject;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;

/**
 * @author Sergey Grigoriev
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/org/springframework/reinject/app-context.xml")
public class XmlConfigTest  {
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
        assertEquals("easyMock", printer.print());
    }

}

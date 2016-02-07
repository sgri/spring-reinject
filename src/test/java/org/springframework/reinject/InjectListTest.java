package org.springframework.reinject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ListFactoryBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/org/springframework/reinject/reinject-context.xml", "classpath:/org/springframework/reinject/InjectListTest-context.xml"})
@DirtiesContext
public class InjectListTest {

    private static final String BEAN_NAME = "injectedList";

    private static final String STUB_ENTRY_1 = "Mesa list entry 1";

    @Autowired
    @Qualifier(BEAN_NAME)
    ListFactoryBean stringList;

    private List<String> listStub = new ArrayList<>();

    public InjectListTest() {
        listStub.add(STUB_ENTRY_1);
        ReInjectPostProcessor.inject(BEAN_NAME, listStub);
    }

    @Test
    public void injectList() throws Exception {
        assertEquals(listStub, stringList.getObject());
    }

}

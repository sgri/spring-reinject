package org.springframework.reinject;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.MapFactoryBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/org/springframework/reinject/reinject-context.xml", "classpath:/org/springframework/reinject/InjectMapTest-context.xml"})
@DirtiesContext
public class InjectMapTest {

    private static final String BEAN_NAME = "injectedMap";

    private static final String STUB_KEY_1 = "MESA.KEY";

    private static final String STUB_VALUE_1 = "JarJar says hello";

    @Autowired
    @Qualifier(BEAN_NAME)
    MapFactoryBean injectedMap;

    private Map<String, String> mapStub = new HashMap<>();

    public InjectMapTest() {
        mapStub.put(STUB_KEY_1, STUB_VALUE_1);
        ReInjectPostProcessor.inject(BEAN_NAME, mapStub);
    }

    @Test
    public void injectMap() throws Exception {
        assertEquals(mapStub, injectedMap.getObject());
    }

}

package org.springframework.reinject;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import junit.framework.Assert;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@DirtiesContext
public class InjectMapTest {
	
	private static final String BEAN_NAME = "stringStringMap";

	private static final String STUB_KEY_1 = "MESA.KEY";

	private static final String STUB_VALUE_1 = "JarJar says hello";

	@Autowired
	private ApplicationContext applicationContext;
	
	private Map<String, String> mapStub = new HashMap<>();
	
	public InjectMapTest() {
		mapStub.put(STUB_KEY_1, STUB_VALUE_1);
		ReInjectPostProcessor.inject(BEAN_NAME, Map.class, mapStub);
	}
	
	@Test
	public void testInjectMap() throws Exception {
		@SuppressWarnings("unchecked")
		Map<String, String> actualMap = (Map<String, String>) applicationContext.getBean(BEAN_NAME);
		String actual = actualMap.get(STUB_KEY_1);
		String expected = STUB_VALUE_1;
		Assert.assertEquals(expected, actual);
	}

}

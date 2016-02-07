package org.springframework.reinject;

import java.util.ArrayList;
import java.util.List;
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
public class InjectListTest {
	
	private static final String BEAN_NAME = "stringList";

	private static final String STUB_ENTRY_1 = "Mesa list entry 1";

	@Autowired
	private ApplicationContext applicationContext;
	
	private List<String> listStub = new ArrayList<>();
	
	public InjectListTest() {
		listStub.add(STUB_ENTRY_1);
		ReInjectPostProcessor.inject(BEAN_NAME, List.class, listStub);
	}
	
	@Test
	public void testInjectMap() throws Exception {
		@SuppressWarnings("unchecked")
		List<String> actualList = (List<String>) applicationContext.getBean(BEAN_NAME);
		Assert.assertEquals("Empty list should have been reinjected!", 1,  actualList.size());
		String actual = actualList.get(0);
		String expected = STUB_ENTRY_1;
		Assert.assertEquals(expected, actual);
	}

}

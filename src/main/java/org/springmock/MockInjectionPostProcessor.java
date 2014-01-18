package org.springmock;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.stereotype.Component;

/**
 * @author Sergey Grigoriev
 */
@Component
public class MockInjectionPostProcessor implements BeanFactoryPostProcessor, DisposableBean {
    private static MockInjectionPostProcessor instance;
    private static final Map<String, Class> mocksByName = new LinkedHashMap<>();

    public static void injectBean(String name, Class clazz) {
        mocksByName.put(name, clazz);
    }


    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        for (String s : beanFactory.getBeanDefinitionNames()) {
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(s);
            if (mocksByName.containsKey(s)) {
                if (beanDefinition instanceof GenericBeanDefinition)  {
                    GenericBeanDefinition gbd = (GenericBeanDefinition) beanDefinition;
                    gbd.setBeanClass(mocksByName.get(s));
                }
            }
        }
    }

    @Override
    public void destroy() throws Exception {
        mocksByName.clear();
    }
}

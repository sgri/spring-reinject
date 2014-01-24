package org.springframework.reinject;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.inject.Provider;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.stereotype.Component;

/**
 * @author Sergey Grigoriev
 */
@Component
public class MockInjectionPostProcessor implements BeanFactoryPostProcessor, DisposableBean {
    private static MockInjectionPostProcessor instance;
    private static final Map<String, Class> mocksByName = new LinkedHashMap<>();
    private static final Map<String, Provider> providersByName = new LinkedHashMap<>();

    public static void inject(String name, Class clazz) {
        mocksByName.put(name, clazz);
    }

    public static <T> void inject(String name, Provider<T> provider) {
        providersByName.put(name, provider);
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        Map<String, BeanDefinition> toRemove = new LinkedHashMap<>();
        for (String s : beanFactory.getBeanDefinitionNames()) {
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(s);
            if (mocksByName.containsKey(s)) {
                if (beanDefinition instanceof GenericBeanDefinition)  {
                    GenericBeanDefinition gbd = (GenericBeanDefinition) beanDefinition;
                    gbd.setBeanClass(mocksByName.get(s));
                } else {
                    toRemove.put(s, beanDefinition);
                }
            }
        }
        for (Map.Entry<String, BeanDefinition> entry : toRemove.entrySet()) {
            BeanDefinitionRegistry bdr = (BeanDefinitionRegistry) beanFactory;
            String beanName = entry.getKey();
            GenericBeanDefinition gbd = new GenericBeanDefinition(entry.getValue());
            gbd.setFactoryBeanName(null);
            gbd.setFactoryMethodName(null);
            gbd.setBeanClass(mocksByName.get(beanName));
            bdr.removeBeanDefinition(beanName);
            bdr.registerBeanDefinition(beanName, gbd);
        }
    }

    @Override
    public void destroy() throws Exception {
        mocksByName.clear();
    }
}

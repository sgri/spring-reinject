package org.springframework.reinject;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.stereotype.Component;

/**
 * @author Sergey Grigoriev
 */
@Component
public class ReInjectPostProcessor implements BeanFactoryPostProcessor, DisposableBean {
    private static final Map<String, Class> mocksByName = new LinkedHashMap<>();
    private static final Map<String, Object> objectsByName = new LinkedHashMap<>();

    public static void inject(String name, Class clazz) {
        mocksByName.put(name, clazz);
    }

    public static <T> void inject(String name, Object object) {
        objectsByName.put(name, object);
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        Map<String, BeanDefinition> toRemove = new LinkedHashMap<>();
        Map<String, BeanDefinition> toAdd = new LinkedHashMap<>();
        for (String s : beanFactory.getBeanDefinitionNames()) {
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(s);
            if (mocksByName.containsKey(s)) {
                if (beanDefinition instanceof GenericBeanDefinition)  {
                    GenericBeanDefinition gbd = (GenericBeanDefinition) beanDefinition;
                    gbd.setBeanClass(mocksByName.get(s));
                    toAdd.put(s, gbd);
                } else {
                    GenericBeanDefinition gbd = new GenericBeanDefinition(beanDefinition);
                    gbd.setFactoryBeanName(null);
                    gbd.setFactoryMethodName(null);
                    gbd.setBeanClass(mocksByName.get(s));
                    toAdd.put(s, gbd);
                    toRemove.put(s, beanDefinition);
                }
            } else if (objectsByName.containsKey(s)) {
                GenericBeanDefinition gbd = new GenericBeanDefinition(beanDefinition);
                gbd.setBeanClass(ReInjectFactoryBean.class);
                gbd.setBeanClassName(null);
                ConstructorArgumentValues constructorArgumentValues = new ConstructorArgumentValues();
                constructorArgumentValues.addGenericArgumentValue(objectsByName.get(s));
                constructorArgumentValues.addGenericArgumentValue(null);
                toRemove.put(s, beanDefinition);
                toAdd.put(s, gbd);
            }
        }
        for (Map.Entry<String, BeanDefinition> entry : toRemove.entrySet()) {
            BeanDefinitionRegistry bdr = (BeanDefinitionRegistry) beanFactory;
            String beanName = entry.getKey();
            bdr.removeBeanDefinition(beanName);
        }

        for (Map.Entry<String, BeanDefinition> entry : toAdd.entrySet()) {
            BeanDefinitionRegistry bdr = (BeanDefinitionRegistry) beanFactory;
            String beanName = entry.getKey();
            bdr.registerBeanDefinition(beanName, entry.getValue());
        }
    }

    @Override
    public void destroy() throws Exception {
        mocksByName.clear();
    }
}

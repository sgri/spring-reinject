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
    private static final Map<String, ConstructorArgumentValues> constructorArgsMap = new LinkedHashMap<>();

    /**
     * Replace a bean definition with a another class
     * @param name bean id
     * @param clazz new class which replaces the original bean definition class
     */
    public static void inject(String name, Class clazz) {
        mocksByName.put(name, clazz);
    }

    /**
     * Replace a bean definition with a factory bean which returns a pre-instantiated object
     * @param name bean id
     * @param beanType object type, see {@linkplain org.springframework.beans.factory.FactoryBean#getObjectType()}
     * @param object bean instance
     */
    public static void inject(String name, Class  beanType, Object object) {
        if (beanType.isAssignableFrom(object.getClass()))
        objectsByName.put(name, object);
        ConstructorArgumentValues constructorArgumentValues = new ConstructorArgumentValues();
        constructorArgumentValues.addGenericArgumentValue(object);
        constructorArgumentValues.addGenericArgumentValue(beanType);
        constructorArgsMap.put(name, constructorArgumentValues);
    }

    /**
     * See {@linkplain #inject(String, Class, Object)}
     */
    public static void inject(String name, Object object) {
        inject(name, object.getClass(), object);
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
                gbd.setBeanClassName(ReInjectFactoryBean.class.getName());
                gbd.setFactoryBeanName(null);
                gbd.setFactoryMethodName(null);
                ConstructorArgumentValues constructorArgumentValues = constructorArgsMap.get(s);
                gbd.setConstructorArgumentValues(constructorArgumentValues);
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
        objectsByName.clear();
        constructorArgsMap.clear();
    }
}

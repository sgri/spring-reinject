package org.springframework.reinject;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.BeansException;
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
public class ReInjectPostProcessor implements BeanFactoryPostProcessor {
    private static final Map<String, Class> mocksByName = new LinkedHashMap<>();
    private static final Map<String, Object> objectsByName = new LinkedHashMap<>();
    private static final Map<String, ConstructorArgumentValues> constructorArgsMap = new LinkedHashMap<>();

    /**
     * Replace a bean definition with a another class
     *
     * @param name  bean id
     * @param clazz new class which replaces the original bean definition class
     */
    public static void inject(String name, Class clazz) {
        mocksByName.put(name, clazz);
    }

    /**
     * Replace a bean definition with a factory bean which returns a pre-instantiated object
     *
     * @param name     bean id
     * @param beanType object type, see {@linkplain org.springframework.beans.factory.FactoryBean#getObjectType()}
     * @param object   bean instance
     */
    public static void inject(String name, Class beanType, Object object) {
        if (!beanType.isAssignableFrom(object.getClass())) {
            throw new IllegalArgumentException(String.format("%s is not assignable form %s", beanType.getClass().getName(), object.getClass().getName()));
        }
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
        for (String beanName : beanFactory.getBeanDefinitionNames()) {
            BeanDefinition bd = beanFactory.getBeanDefinition(beanName);
            if (mocksByName.containsKey(beanName) || objectsByName.containsKey(beanName)) {
                GenericBeanDefinition overriddenBd = new GenericBeanDefinition(bd);
                overriddenBd.setFactoryBeanName(null);
                overriddenBd.setFactoryMethodName(null);
                if (mocksByName.containsKey(beanName)) {
                    overriddenBd.setBeanClass(mocksByName.get(beanName));
                } else if (objectsByName.containsKey(beanName)) {
                    overriddenBd.setBeanClassName(ReInjectFactoryBean.class.getName());
                    ConstructorArgumentValues constructorArgumentValues = constructorArgsMap.get(beanName);
                    overriddenBd.setConstructorArgumentValues(constructorArgumentValues);
                }
                BeanDefinitionRegistry bdr = (BeanDefinitionRegistry) beanFactory;
                bdr.removeBeanDefinition(beanName);
                bdr.registerBeanDefinition(beanName, overriddenBd);
            }
        }
        cleanup();
    }

    private void cleanup()  {
        mocksByName.clear();
        objectsByName.clear();
        constructorArgsMap.clear();
    }
}

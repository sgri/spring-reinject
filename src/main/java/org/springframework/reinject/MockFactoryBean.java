package org.springframework.reinject;

import org.springframework.beans.factory.FactoryBean;

/**
 * @author Sergey Grigoriev
 */
public class MockFactoryBean implements FactoryBean {
    private final Object object;
    private final Class objectType;

    public MockFactoryBean(Object object, Class objectType) {
        this.object = object;
        this.objectType = objectType;
    }

    @Override
    public Object getObject() throws Exception {
        return object;
    }

    @Override
    public Class<?> getObjectType() {
        return objectType == null ? object.getClass(): objectType;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}

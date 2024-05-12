package com.purplecloud.bean;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * @author 15517
 */
public class BeanDefinition {
    String name;

    Class<?> beanClass;

    Object instance = null;

    Constructor<?> constructor;

    String factoryName;

    Method factoryMethod;

    int order;

    boolean primary;

    String initMethodName;
    String destroyMethodName;

    Method initMethod;
    Method destroyMethod;

    public BeanDefinition() {
    }

    public BeanDefinition(String name,
                          Class<?> beanClass,
                          Object instance,
                          Constructor<?> constructor,
                          String factoryName,
                          Method factoryMethod,
                          int order,
                          boolean primary,
                          String initMethodName,
                          String destroyMethodName,
                          Method initMethod,
                          Method destroyMethod) {
        this.name = name;
        this.beanClass = beanClass;
        this.instance = instance;
        this.constructor = constructor;
        this.factoryName = factoryName;
        this.factoryMethod = factoryMethod;
        this.order = order;
        this.primary = primary;
        this.initMethodName = initMethodName;
        this.destroyMethodName = destroyMethodName;
        this.initMethod = initMethod;
        this.destroyMethod = destroyMethod;
    }
    public BeanDefinition(String name,
                          Class<?> beanClass,
                          int order,
                          boolean primary,
                          String initMethodName,
                          String destroyMethodName,
                          Method initMethod,
                          Method destroyMethod) {
        this.name = name;
        this.beanClass = beanClass;
        this.order = order;
        this.primary = primary;
        this.initMethodName = initMethodName;
        this.destroyMethodName = destroyMethodName;
        this.initMethod = initMethod;
        this.destroyMethod = destroyMethod;
    }

    public String getName() {
        return name;
    }

    /**
     * 设置
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取
     * @return beanClass
     */
    public Class<?> getBeanClass() {
        return beanClass;
    }

    /**
     * 设置
     * @param beanClass
     */
    public void setBeanClass(Class<?> beanClass) {
        this.beanClass = beanClass;
    }

    /**
     * 获取
     * @return instance
     */
    public Object getInstance() {
        return instance;
    }

    /**
     * 设置
     * @param instance
     */
    public void setInstance(Object instance) {
        this.instance = instance;
    }

    /**
     * 获取
     * @return constructor
     */
    public Constructor<?> getConstructor() {
        return constructor;
    }

    /**
     * 设置
     * @param constructor
     */
    public void setConstructor(Constructor<?> constructor) {
        this.constructor = constructor;
    }

    /**
     * 获取
     * @return factoryName
     */
    public String getFactoryName() {
        return factoryName;
    }

    /**
     * 设置
     * @param factoryName
     */
    public void setFactoryName(String factoryName) {
        this.factoryName = factoryName;
    }

    /**
     * 获取
     * @return factoryMethod
     */
    public Method getFactoryMethod() {
        return factoryMethod;
    }

    /**
     * 设置
     * @param factoryMethod
     */
    public void setFactoryMethod(Method factoryMethod) {
        this.factoryMethod = factoryMethod;
    }

    /**
     * 获取
     * @return order
     */
    public int getOrder() {
        return order;
    }

    /**
     * 设置
     * @param order
     */
    public void setOrder(int order) {
        this.order = order;
    }

    /**
     * 获取
     * @return primary
     */
    public boolean isPrimary() {
        return primary;
    }

    /**
     * 设置
     * @param primary
     */
    public void setPrimary(boolean primary) {
        this.primary = primary;
    }

    /**
     * 获取
     * @return initMethodName
     */
    public String getInitMethodName() {
        return initMethodName;
    }

    /**
     * 设置
     * @param initMethodName
     */
    public void setInitMethodName(String initMethodName) {
        this.initMethodName = initMethodName;
    }

    /**
     * 获取
     * @return destroyMethodName
     */
    public String getDestroyMethodName() {
        return destroyMethodName;
    }

    /**
     * 设置
     * @param destroyMethodName
     */
    public void setDestroyMethodName(String destroyMethodName) {
        this.destroyMethodName = destroyMethodName;
    }

    /**
     * 获取
     * @return initMethod
     */
    public Method getInitMethod() {
        return initMethod;
    }

    /**
     * 设置
     * @param initMethod
     */
    public void setInitMethod(Method initMethod) {
        this.initMethod = initMethod;
    }

    /**
     * 获取
     * @return destroyMethod
     */
    public Method getDestroyMethod() {
        return destroyMethod;
    }

    /**
     * 设置
     * @param destroyMethod
     */
    public void setDestroyMethod(Method destroyMethod) {
        this.destroyMethod = destroyMethod;
    }

    public String toString() {
        return "BeanDefinition{name = " + name + ", beanClass = " + beanClass + ", instance = " + instance + ", constructor = " + constructor + ", factoryName = " + factoryName + ", factoryMethod = " + factoryMethod + ", order = " + order + ", primary = " + primary + ", initMethodName = " + initMethodName + ", destroyMethodName = " + destroyMethodName + ", initMethod = " + initMethod + ", destroyMethod = " + destroyMethod + "}";
    }
}

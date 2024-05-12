package com.purplecloud.main;


import com.purplecloud.annotation.Autowired;
import com.purplecloud.annotation.Component;
import com.purplecloud.annotation.Primary;
import com.purplecloud.bean.BeanDefinition;
import com.purplecloud.exception.InjectException;
import com.purplecloud.util.ClassUtil;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import static com.purplecloud.util.AnnotationUtil.*;
import static com.purplecloud.util.ClassUtil.findAnnotationMethod;
import static com.purplecloud.util.ClassUtil.hasAnnotation;

/**
 * @author 15517
 */
public class AnnotationConfigApplicationContext {
    protected final MyResourceResolver propertyResolver;
    protected final Map<String, BeanDefinition> beans;

    public AnnotationConfigApplicationContext(MyResourceResolver propertyResolver, Map<String, BeanDefinition> beans) {
        this.propertyResolver = propertyResolver;
        this.beans = beans;
    }
    /**
     * 初始化反转控制
     */
    public AnnotationConfigApplicationContext(MyResourceResolver propertyResolver) {
        this.propertyResolver = propertyResolver;
        //创建所有的反转控制的BeanDefinition
        beans = createBeanDefinitions(propertyResolver.getClasses());
        try {
            //
            newInstantFromOrder();
            autoInject();
            autoIni();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    Map<String, BeanDefinition> createBeanDefinitions(List<String> classNames) {
        Map<String, BeanDefinition> beans = new HashMap<>();
        for (String className : classNames) {
            Class<?> aClass;
            try {
                //创建类
                ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                aClass = classLoader.loadClass(className);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }//判断有没有Component在controller中的也可以检测不过只能检测二级
            if (hasAnnotation(aClass, Component.class)&&(!aClass.isAnnotation())) {
                BeanDefinition beanDefinition = new BeanDefinition(
                        getComponent(aClass),
                        aClass,
                        getOrder(aClass),
                        hasAnnotation(aClass, Primary.class),
                        null,
                        null,
                        findAnnotationMethod(aClass, PostConstruct.class),
                        ClassUtil.findAnnotationMethod(aClass, PreDestroy.class)
                );
                beans.put(getComponent(aClass), beanDefinition);
            }
        }
        return beans;
    }
    /**
     * 按顺序床创建实体
     */
    void newInstantFromOrder() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {

        List<BeanDefinition> beanDefinitions = new ArrayList<>(this.beans.values());
        //排序按照order
        beanDefinitions.sort(Comparator.comparingInt(BeanDefinition::getOrder));
        //批量创建对象
        for (BeanDefinition beanDefinition : beanDefinitions) {
            Object instance = beanDefinition.getBeanClass().getConstructor().newInstance();
            beanDefinition.setInstance(instance);
        }
    }
    /**
     * 扫描所有需要注入的并进行注入不支持父类注入
     */
    void autoInject() throws IllegalAccessException, InjectException {
        for (BeanDefinition beanDefinition : beans.values()) {
            Object instance = beanDefinition.getInstance();
            for (Field declaredField : instance.getClass().getDeclaredFields()) {
                if (hasAnnotation(declaredField, Autowired.class)) {
                    String name = getAutowired(declaredField);
                    BeanDefinition definition = beans.get(name);
                    if (definition != null) {
                        declaredField.setAccessible(true);
                        declaredField.set(instance, definition.getInstance());
                    } else {
                        throw new InjectException("could not find" + declaredField.getName() + "to inject");
                    }
                }
            }
        }
    }
    /**
     * 自动初始化实体比如连接池等需要
     */
    void autoIni() throws InvocationTargetException, IllegalAccessException {
        for (BeanDefinition definition : beans.values()) {
            Method initMethod = definition.getInitMethod();
            if (initMethod!=null){
                initMethod.invoke(definition.getInstance(),null);
            }
        }
    }
    public Map<String, BeanDefinition> getBeans(){
        return this.beans;
    }
}

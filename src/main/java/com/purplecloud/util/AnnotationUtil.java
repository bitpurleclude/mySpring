package com.purplecloud.util;


import com.purplecloud.annotation.Autowired;
import com.purplecloud.annotation.Component;
import com.purplecloud.annotation.Order;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
/**
 * 获得各种注解的工具类
 * @author 15517
 */
public class AnnotationUtil {

    public static int getOrder(Class<?> clazz){
        if (clazz.isAnnotationPresent(Order.class)){
            Order annotation = clazz.getAnnotation(Order.class);
            return annotation.value();
        }
        return 0;
    }
    public static String getAutowired(Field field){
        if (field.isAnnotationPresent(Autowired.class)){
            if (!field.getAnnotation(Autowired.class).name().isEmpty()){
                return field.getAnnotation(Autowired.class).name();
            }
        }
        return field.getType().getSimpleName();
    }
    public static String getComponent(Class<?> clazz){
        if (clazz.isAnnotationPresent(Component.class)){
            if (!clazz.getAnnotation(Component.class).value().isEmpty()){
                return clazz.getAnnotation(Component.class).value();
            }
        }
        return clazz.getSimpleName();
    }
    public static Method findAnnotationMethod(Class<?> clazz, Class<?extends Annotation> anno){

        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(anno)){
                method.setAccessible(true);
             return method;
            }
        }
        return null;
    }
}

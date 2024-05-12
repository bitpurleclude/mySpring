package com.purplecloud.util;


import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;

/**
 * 判断有无注解和获得类之中的注解
 */
public class ClassUtil {
    @Nullable
    public static <T extends Annotation> T findAnnotation(Class<?> clazz, Class<T> anno) {
        return (T) clazz.getAnnotation(anno);
    }

    @Nullable
    public static Method findAnnotationMethod(Class<?> clazz, Class<? extends Annotation> annoClass) {
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            if (hasAnnotation(method, annoClass)) {
                return method;
            }
        }
        return null;
    }

    public static <A extends Annotation> A getAnnotation(Annotation[] annotations, Class<A> aClass) {
        for (Annotation anno : annotations) {
            if (aClass.isInstance(anno)) {
                return (A) anno;
            }
        }
        return null;

    }

    public static <T extends AnnotatedElement, A extends Annotation> boolean hasAnnotation(T t, Class<A> anno) {
        if (t.isAnnotationPresent(anno)) {
            return true;
        } else {
            Annotation[] annotations = t.getAnnotations();
            for (Annotation annotation : annotations) {
                if (annotationHasAnnotation(annotation, anno)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static <A extends Annotation> boolean annotationHasAnnotation(Annotation annotation, Class<A> anno) {
        return annotation.annotationType().isAnnotationPresent(anno);
    }

}

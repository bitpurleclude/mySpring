package com.purplecloud.util;


import com.purplecloud.main.AnnotationConfigApplicationContext;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
/**
 * 存放反转控制的库的
 * @author 15517
 */
public class ApplicationContextUtils {
    private static AnnotationConfigApplicationContext annotationConfigApplicationContext = null;
    public static AnnotationConfigApplicationContext getRequiredApplicationContext() {
        return Objects.requireNonNull(getApplicationContext(), "ApplicationContext is not set.");
    }

    @Nullable
    public static AnnotationConfigApplicationContext getApplicationContext() {
        return annotationConfigApplicationContext;
    }

    public static void setApplicationContext(AnnotationConfigApplicationContext ctx) {
        annotationConfigApplicationContext = ctx;
    }
}

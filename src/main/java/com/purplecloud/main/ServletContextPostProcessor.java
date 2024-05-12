package com.purplecloud.main;

import javax.servlet.ServletContext;

/**
 * @author 15517
 */
public class ServletContextPostProcessor {
    static ServletContext servletContext;

    public Object postProcessBeforeInitialization(Object bean) {
        if (bean instanceof ServletContext) {
            return servletContext;
        }
        return bean;
    }
    public static void setServletContext(ServletContext ctx) {
        servletContext = ctx;
    }
}

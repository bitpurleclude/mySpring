package com.purplecloud.main;




import com.purplecloud.util.ApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;


/**
 * @author 15517
 */
@WebListener
public class MyListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        //初始化反转控制和dispatcherServlet
        ServletContext servletContext = sce.getServletContext();
        ServletContextPostProcessor.setServletContext(servletContext);
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(new MyResourceResolver("com.lijiarui.www"));
        servletContext.setAttribute("applicationContext", applicationContext);
        ApplicationContextUtils.setApplicationContext(applicationContext);
        var dispatcherServlet = new DispatcherServlet(ApplicationContextUtils.getRequiredApplicationContext());
        var dispatcherReg = servletContext.addServlet("dispatcherServlet", dispatcherServlet);
        dispatcherReg.addMapping("/");
        dispatcherReg.setLoadOnStartup(1);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ServletContextListener.super.contextDestroyed(sce);
    }
}

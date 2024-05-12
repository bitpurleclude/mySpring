package com.purplecloud.main;


import com.alibaba.fastjson.JSON;
import com.purplecloud.annotation.Controller;
import com.purplecloud.annotation.GetMapping;
import com.purplecloud.annotation.PostMapping;
import com.purplecloud.bean.BeanDefinition;
import com.purplecloud.bean.Result;
import com.purplecloud.util.ApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
/**
 * @author 15517
 */
public class DispatcherServlet extends HttpServlet {

    AnnotationConfigApplicationContext applicationContext;
    /**
     * 访问页面的位置
     */
    String resourcePath="/my_zhihu_war_exploded/resource";
    /**
     * 处理需求的位置
     */
    String Path="/my_zhihu_war_exploded";
    List<Dispatcher> getDispatchers = new ArrayList<>();
    List<Dispatcher> postDispatchers = new ArrayList<>();

    public DispatcherServlet(AnnotationConfigApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
    public DispatcherServlet() {

    }
    /**
     * 初始化所有controller
     */
    @Override
    public void init() throws ServletException {
        // scan @Controller and @RestController:
        for (BeanDefinition beanDefinition : ApplicationContextUtils.getApplicationContext().getBeans().values()) {
            Class<?> beanClass = beanDefinition.getBeanClass();
            Object instance = beanDefinition.getInstance();
            Controller controller = beanClass.getAnnotation(Controller.class);
            if (controller != null) {
                addController(instance);
            }
        }
    }

    void addController(Object instance) throws ServletException {
        addMethods(instance, instance.getClass());
    }
    //将每个方法都放入Dispatcher并完成初始化

    void addMethods(Object instance, Class<?> type) throws ServletException {
        for (Method m : type.getDeclaredMethods()) {
            GetMapping get = m.getAnnotation(GetMapping.class);
            if (get != null) {
                this.getDispatchers.add(new Dispatcher("GET", instance, m, get.value()));
            }
            PostMapping post = m.getAnnotation(PostMapping.class);
            if (post != null) {
                this.postDispatchers.add(new Dispatcher("POST", instance, m, post.value()));
            }
        }
        Class<?> superClass = type.getSuperclass();
        if (superClass != null) {
            addMethods(instance, superClass);
        }
    }


    @Override
    public void destroy() {
    }
    /**
     * get因为有部分是要获取文件的所以判断是否是请求页面
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String url = req.getRequestURI();
        if ( url.startsWith(this.resourcePath)) {
            doResource(url.replace(this.Path,""), req, resp);
        } else {
            doService(req, resp, this.getDispatchers);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        doService(req, resp, this.postDispatchers);
    }

    void doService(HttpServletRequest req, HttpServletResponse resp, List<Dispatcher> dispatchers) {
        String url = req.getRequestURI();
        try {
            doService(url.replace(this.Path,""), req, resp, dispatchers);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    void doService(String url, HttpServletRequest req, HttpServletResponse resp, List<Dispatcher> dispatchers) throws Exception {
        for (Dispatcher dispatcher : dispatchers) {
            Result result = null;
            try {
                result = dispatcher.process(url, req, resp);
            } catch (RuntimeException e) {
                PrintWriter pw = resp.getWriter();
                pw.write(e.getMessage());
                pw.flush();
                throw new ServletException("Incorrect format"+e);
            }
            if (result.isProcessed()) {
                Object r = result.getReturnObject();
                // 返回数据处理
                if (!resp.isCommitted()) {
                    resp.setContentType("application/json;charset=UTF-8");
                }
                if (dispatcher.isResponseBody) {
                    //返回数据
                    if (r instanceof String s) {
                        PrintWriter pw = resp.getWriter();
                        pw.write(s);
                        pw.flush();
                    } else if (r instanceof byte[] data) {
                        ServletOutputStream output = resp.getOutputStream();
                        output.write(data);
                        output.flush();
                    } else {
                        // 错误类型
                        throw new ServletException("Unable to process REST result when handle url: " + url);
                    }
                    //有返回值就转化为json
                } else if (!dispatcher.isVoid) {
                    resp.getWriter().write(JSON.toJSONString(r));
                }
                return;
            }
        }
        // 没有该处理
        resp.sendError(404, "Not Found");
    }
    /**
     * 获取文件
     */
    void doResource(String url, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        ServletContext ctx = req.getServletContext();
        try (InputStream input = ctx.getResourceAsStream(url)) {
            //判断是否有这个文件
            if (input == null) {
                resp.sendError(404, "Not Found");
            } else {
                // guess content type:
                String file = url;
                int n = url.lastIndexOf('/');
                if (n >= 0) {
                    file = url.substring(n + 1);
                }
                String mime = ctx.getMimeType(file);
                if (mime == null) {
                    mime = "application/octet-stream";
                }
                //返回文件
                resp.setContentType(mime);
                ServletOutputStream output = resp.getOutputStream();
                input.transferTo(output);
                output.flush();
            }
        }
    }
}

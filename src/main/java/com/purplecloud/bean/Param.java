package com.purplecloud.bean;


import com.purplecloud.annotation.PathVariable;
import com.purplecloud.annotation.RequestBody;
import com.purplecloud.annotation.RequestParam;
import com.purplecloud.util.ClassUtil;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.ServerErrorException;
import javax.ws.rs.core.Response;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class Param {

    private String name;
    private ParamType paramType;
    private Class<?> classType;
    private String defaultValue;
    private Annotation[] annotations;

    public Param(String httpMethod, Method method, Parameter parameter, Annotation[] annotations) throws ServletException {
        this.annotations = annotations;
        PathVariable pv = ClassUtil.getAnnotation(annotations, PathVariable.class);
        RequestParam rp = ClassUtil.getAnnotation(annotations, RequestParam.class);
        RequestBody rb = ClassUtil.getAnnotation(annotations, RequestBody.class);
        // should only have 1 annotation:
        int total = (pv == null ? 0 : 1) + (rp == null ? 0 : 1) + (rb == null ? 0 : 1);
        if (total > 1) {
            throw new ServletException("Annotation @PathVariable, @RequestParam and @RequestBody cannot be combined at method: " + method);
        }
        this.classType = parameter.getType();
        if (pv != null) {
            this.name = pv.value();
            this.paramType = ParamType.PATH_VARIABLE;
        } else if (rp != null) {
            this.name = rp.value();
            //this.defaultValue = rp.defaultValue();
            this.paramType = ParamType.REQUEST_PARAM;
        } else if (rb != null) {
            this.paramType = ParamType.REQUEST_BODY;
        } else {
            this.paramType = ParamType.SERVLET_VARIABLE;
            // check servlet variable type:
            if (this.classType != HttpServletRequest.class && this.classType != HttpServletResponse.class && this.classType != HttpSession.class
                    && this.classType != ServletContext.class) {
                throw new ServerErrorException(Response.Status.valueOf("(Missing annotation?) Unsupported argument type: " + classType + " at method: " + method));
            }
        }
    }

    public Param() {
    }

    @Override
    public String toString() {
        return "Param [name=" + name + ", paramType=" + paramType + ", classType=" + classType + ", defaultValue=" + defaultValue + "]";
    }

    /**
     * 获取
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    public Annotation[] getAnnotations() {
        return annotations;
    }

    /**
     * 设置
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取
     *
     * @return paramType
     */
    public ParamType getParamType() {
        return paramType;
    }

    /**
     * 设置
     *
     * @param paramType
     */
    public void setParamType(ParamType paramType) {
        this.paramType = paramType;
    }

    /**
     * 获取
     *
     * @return classType
     */
    public Class<?> getClassType() {
        return classType;
    }

    /**
     * 设置
     *
     * @param classType
     */
    public void setClassType(Class<?> classType) {
        this.classType = classType;
    }

    /**
     * 获取
     *
     * @return defaultValue
     */
    public String getDefaultValue() {
        return defaultValue;
    }

    /**
     * 设置
     *
     * @param defaultValue
     */
    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }
}

package com.purplecloud.main;


import com.alibaba.fastjson.JSON;
import com.purplecloud.annotation.LegalFormat;
import com.purplecloud.annotation.LimitAccess;
import com.purplecloud.annotation.ResponseBody;
import com.purplecloud.bean.APICounter;
import com.purplecloud.bean.Param;
import com.purplecloud.bean.Result;
import com.purplecloud.exception.APITooFrequentException;
import com.purplecloud.util.ClassUtil;
import com.purplecloud.util.PathUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
/**
 * 每个controller的每个方法都有这个处理类用于存储各种信息
 */
public class Dispatcher {

    final static Result NOT_PROCESSED = new Result(false, null);
    APICounter apiCounter;

    boolean isResponseBody;
    boolean isVoid;
    Pattern urlPattern;
    Object controller;
    Method handlerMethod;
    Param[] methodParameters;
    //初始化
    public Dispatcher(String httpMethod, Object controller, Method method, String urlPattern) throws ServletException {
        LimitAccess limitAccess = method.getAnnotation(LimitAccess.class);
        if (limitAccess!=null){
            this.apiCounter=new APICounter(limitAccess.time(),limitAccess.amount());
        }
        this.isResponseBody = method.getAnnotation(ResponseBody.class) != null;
        this.isVoid = method.getReturnType() == void.class;
        this.urlPattern = PathUtils.compile(urlPattern);
        this.controller = controller;
        this.handlerMethod = method;
        Parameter[] params = method.getParameters();
        Annotation[][] paramsAnnos = method.getParameterAnnotations();
        this.methodParameters = new Param[params.length];
        for (int i = 0; i < params.length; i++) {
            this.methodParameters[i] = new Param(httpMethod, method, params[i], paramsAnnos[i]);
        }
    }
    public boolean checkIllegal(Annotation[] annotations, String s){
        LegalFormat legalFormat = ClassUtil.getAnnotation(annotations, LegalFormat.class);
        if (legalFormat!=null){
            String format = legalFormat.value();
            return Pattern.matches(format, s);
        }
        return true;
    }
    /**
     * 处理需求
     */
    Result process(String url, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Matcher matcher = urlPattern.matcher(url);
        if (matcher.matches()) {
            if (this.apiCounter!=null){
                if (!this.apiCounter.usedMoth()){
                    throw  new APITooFrequentException(this.handlerMethod.getName(),this.apiCounter.getLimit());
                }
            }
            Object[] arguments = new Object[this.methodParameters.length];
            for (int i = 0; i < arguments.length; i++) {
                Param param = methodParameters[i];
                try {
                switch (param.getParamType()) {
                    //？asdjkhlf=asf获取的就是等号后的数据
                    case PATH_VARIABLE -> {
                        try {
                            String s = matcher.group(param.getName());
                            if (checkIllegal(param.getAnnotations(), s)) {
                                arguments[i] = convertToType(param.getClassType(), s);
                            } else {
                                throw new RuntimeException("Incorrect format");
                            }
                        } catch (IllegalArgumentException ignored) {
                            throw new ServletException("Unable to process REST result when handle url: " + url);
                        }
                    }
                    //自动把传入的类转化为对应对象多种信息 杂糅的不行
                    case REQUEST_BODY -> {
                        BufferedReader reader = request.getReader();
                        String jsonStr = reader.lines().collect(Collectors.joining());
                        try {
                            arguments[i] = JSON.parseObject(jsonStr, param.getClassType());
                        } catch (Exception e) {
                            throw new ServletException("The JSON cannot be successfully converted" + e);
                        }
                    }
                    //post下传输的数据
                    case REQUEST_PARAM -> {
                        String s = getOrDefault(request, param.getName());
                        if (checkIllegal(param.getAnnotations(), s)) {
                            arguments[i] = convertToType(param.getClassType(), s);
                        } else {
                            throw new RuntimeException("Incorrect format");
                        }
                    }
                    //SERVLET自带的类型
                    case SERVLET_VARIABLE -> {
                        Class<?> classType = param.getClassType();
                        if (classType == HttpServletRequest.class) {
                            arguments[i] = request;
                        } else if (classType == HttpServletResponse.class) {
                            arguments[i] = response;
                        } else if (classType == HttpSession.class) {
                            arguments[i] = request.getSession();
                        } else if (classType == ServletContext.class) {
                            arguments[i] = request.getServletContext();
                        }
                    }
                    default -> {
                    }
                }
                }catch (RuntimeException e){
                    throw new RuntimeException("Incorrect format");
                }
            }
            Object result = null;
            try {
                result = this.handlerMethod.invoke(this.controller, arguments);
            } catch (InvocationTargetException e) {
                Throwable t = e.getCause();
                if (t instanceof Exception ex) {
                    throw ex;
                }
                throw e;
            } catch (ReflectiveOperationException ignored) {

            }
            return new Result(true, result);
        }
        return NOT_PROCESSED;
    }
    /**
     * 转化类型
     */
    Object convertToType(Class<?> classType, String s) {
        if (classType == String.class) {
            return s;
        } else if (classType == boolean.class || classType == Boolean.class) {
            return Boolean.valueOf(s);
        } else if (classType == int.class || classType == Integer.class) {
            return Integer.valueOf(s);
        } else if (classType == long.class || classType == Long.class) {
            return Long.valueOf(s);
        } else if (classType == byte.class || classType == Byte.class) {
            return Byte.valueOf(s);
        } else if (classType == short.class || classType == Short.class) {
            return Short.valueOf(s);
        } else if (classType == float.class || classType == Float.class) {
            return Float.valueOf(s);
        } else if (classType == double.class || classType == Double.class) {
            return Double.valueOf(s);
        } else {
            return null;
        }
    }
    /**
     * 名字在返回
     */
    String getOrDefault(HttpServletRequest request, String name) {
        return request.getParameter(name);
    }
}

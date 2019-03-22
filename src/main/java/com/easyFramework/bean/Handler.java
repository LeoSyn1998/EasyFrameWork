package com.easyFramework.bean;

import java.lang.reflect.Method;

/**
 * Created on 2018/8/9 0:35.
 *
 * @author SinKitwah
 * @Description 封装RequestMapping方法
 */
public class Handler {

    /**
     * Controller类
     */
    private Class<?> controllerClass;

    /**
     * RequestMapping方法
     */
    private Method requestMappingMethod;

    public Handler(Class<?> controllerClass, Method requestMappingMethod) {
        this.controllerClass = controllerClass;
        this.requestMappingMethod = requestMappingMethod;
    }

    public Class<?> getControllerClass() {
        return controllerClass;
    }

    public Method getRequestMappingMethod() {
        return requestMappingMethod;
    }
}

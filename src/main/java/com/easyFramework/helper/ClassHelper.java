package com.easyFramework.helper;

import com.easyFramework.annotation.Controller;
import com.easyFramework.annotation.Proxy;
import com.easyFramework.annotation.Service;
import com.easyFramework.util.ClassUtil;
import com.easyFramework.util.PropertiesUtil;

import java.util.HashSet;
import java.util.Set;

/**
 * Created on 2018/8/8 21:57.
 *
 * @author SinKitwah
 * @Description 类操作助手类
 */
public class ClassHelper {

    /**
     * 定义类集合（用于存放所加载的类）
     */
    private static final Set<Class<?>> CLASS_SET;

    static {
        String basePackage = PropertiesUtil.getBasePackage();
        CLASS_SET = ClassUtil.getClassSet(basePackage);
    }

    /**
     * 获取所有类
     * @return 所有类
     */
    public static Set<Class<?>> getAllClass() {
        return CLASS_SET;
    }

    /**
     * 获取所有注解Service的类
     * @return 所有注解Service的类
     */
    public static Set<Class<?>> getServiceClass() {
        Set<Class<?>> classSet = new HashSet<Class<?>>();
        for (Class<?> myClass : CLASS_SET) {
            if (myClass.isAnnotationPresent(Service.class)) {//判断这个类是否带有Service注解
                classSet.add(myClass);
            }
        }
        return classSet;
    }

    /**
     * 获取所有注解Controller的类
     * @return 所有注解Controller的类
     */
    public static Set<Class<?>> getControllerClass() {
        Set<Class<?>> classSet = new HashSet<Class<?>>();
        for (Class<?> myClass : CLASS_SET) {
            if (myClass.isAnnotationPresent(Controller.class)) { //判断这个类是否带有Controller注解
                classSet.add(myClass);
            }
        }
        return classSet;
    }

    /**
     * 获取所有带Proxy注解的类（切面类）
     * @return 带Proxy注解的类
     */
    public static Set<Class<?>> getProxyClass() {
        Set<Class<?>> classSet = new HashSet<Class<?>>();
        for (Class<?> myClass : CLASS_SET) {
            if (myClass.isAnnotationPresent(Proxy.class)) {
                classSet.add(myClass);
            }
        }
        return classSet;
    }

    /**
     * 获取所有的Bean类
     * @return 所有带有注解的Bean类
     */
    public static Set<Class<?>> getBeanClass() {
        Set<Class<?>> classSet = new HashSet<Class<?>>();
        classSet.addAll(getControllerClass());
        classSet.addAll(getServiceClass());
        classSet.addAll(getProxyClass());
        return classSet;
    }
}

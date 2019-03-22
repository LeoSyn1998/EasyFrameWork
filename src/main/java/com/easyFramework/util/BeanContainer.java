package com.easyFramework.util;

import com.easyFramework.helper.ClassHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created on 2018/8/9 23:15.
 *
 * @author SinKitwah
 * @Description 实例化所有类，相当于Bean容器
 */
public class BeanContainer {
    /**
     * 定义Bean映射（用于存放Bean类与Bean实例的映射关系）
     */
    private static final Map<Class<?>, Object> BEAN_MAP = new HashMap<Class<?>, Object>();

    static {
        //加载所有的类
        Set<Class<?>> beanClassSet = ClassHelper.getBeanClass();
        for (Class<?> beanClass : beanClassSet){
            Object object = ReflectionUtil.newInstance(beanClass);
            BEAN_MAP.put(beanClass,object);
        }
    }

    /**
     * 获取Bean映射
     * @return Bean映射
     */
    public static Map<Class<?>, Object> getBeanMap(){
        return BEAN_MAP;
    }

}

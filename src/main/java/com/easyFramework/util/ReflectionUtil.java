package com.easyFramework.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created on 2018/8/8 23:43.
 *
 * @author SinKitwah
 * @Description 反射工具类--生成实例并注入属性
 */
public class ReflectionUtil {

    //ReflectionUtil.class的日志
    private static final Logger LOGGER = LoggerFactory.getLogger(ReflectionUtil.class);

    /**
     * 根据传入的Class类创建实例
     * @param cls Class类
     * @return 实例
     */
    public static Object newInstance(Class<?> cls){
        Object instance;
        try {
            instance = cls.newInstance();
        }catch (Exception e){
            LOGGER.error("ReflectionUtil.newInstance: new instance failure",e);
            throw new RuntimeException(e);
        }
        return instance;
    }

    /**
     * 调用方法
     * @param obj 调用方法的对象
     * @param method 被调用的方法
     * @param args 传入参数
     * @return 调用的结果
     */
    public static Object invokeMethod(Object obj, Method method,Object...args){
        Object result = null;
        try {
            //设置可以通过反射获取
            method.setAccessible(true);
            //调用方法
            result = method.invoke(obj, args);
        } catch (Exception e) {
            LOGGER.error("ReflectionUtil.invokeMethod: method invoke is failure",e);
            throw new RuntimeException(e);
        }
        return result;
    }

    /**
     * 设置成员变量
     * @param obj 被设置的对象
     * @param field 成员变量
     * @param value 成员变量的值
     */
    public static void setField(Object obj, Field field,Object value){
        try {
            //设置可以通过反射获取
            field.setAccessible(true);
            //设置属性
            field.set(obj, value);
        } catch (Exception e) {
            LOGGER.error("ReflectionUtil.setField: set object field is failure",e);
            throw new RuntimeException(e);
        }
    }
}

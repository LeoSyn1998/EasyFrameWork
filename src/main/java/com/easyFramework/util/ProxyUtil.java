package com.easyFramework.util;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2018/8/9 20:51.
 *
 * @author SinKitwah
 * @Description 实现动态代理，并生成对应的代理类（CGLib代理模式）
 */
public class ProxyUtil implements MethodInterceptor {

    //创建ProxyUtil代理的日志
    private static final Logger LOGGER = LoggerFactory.getLogger(ProxyUtil.class);

    /**
     * 用于增强指定对象的增强类的对象
     */
    private List<Object> proxyObjectList;
    /**
     * 用于增强指定对象的增强类
     */
    private List<Class<?>> proxyClassList;

    public ProxyUtil(List<Class<?>> proxyClassList) {
        this.proxyClassList = proxyClassList;
    }

    /**
     * 获取代理后的对象
     * @param cls 被代理的类
     * @param <T>
     * @return 代理后的对象
     */
    public <T> T getProxy(Class<T> cls){
        return (T) Enhancer.create(cls,this);
    }

    @Override
    public Object intercept(Object target, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        //实例化所有的增强类
        proxyObjectList = getInstanceList(proxyClassList);
        //返回的结果
        Object result;
        try {
            invokeMethods(proxyObjectList,"before");
            result = methodProxy.invokeSuper(target,args);
            invokeMethods(proxyObjectList,"after");
        }catch (Exception e){
            invokeMethods(proxyObjectList,"afterThrowing");
            throw e;
        }finally {
            invokeMethods(proxyObjectList,"afterReturning");
        }
        return result;
    }

    /**
     * 获取指定对象集合中指定方法并调用
     * @param objects 指定对象集合
     * @param methodName 方法名
     * @return
     */
    private void invokeMethods(List<Object> objects,String methodName){
        try {
            for (Object object:objects){
                Method[] methods = object.getClass().getMethods();
                for (Method method:methods){
                    String name = method.getName();
                    if (name.equals(methodName)){
                        method.invoke(object,null);
                    }
                }
            }
        }catch (Exception e){
            LOGGER.error("ProxyUtil.invokeMethods: invoke AOP methods failure" ,e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 根据class集合获取集合对象
     * @param proxyClassList class集合
     * @return 对象集合
     */
    private List<Object> getInstanceList(List<Class<?>> proxyClassList){
        List<Object> objectList = new ArrayList<Object>();
        try {
            for (Class<?> cls : proxyClassList){
                Object object = cls.newInstance();
                objectList.add(object);
            }
        } catch (Exception e) {
            LOGGER.error("ProxyUtil.getInstanceList:get proxy class failure",e);
            throw new RuntimeException(e);
        }
        return objectList;
    }


}

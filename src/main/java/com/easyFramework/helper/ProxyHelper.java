package com.easyFramework.helper;

import com.easyFramework.annotation.Proxy;
import com.easyFramework.util.BeanContainer;
import com.easyFramework.util.ProxyUtil;

import java.util.*;

/**
 * Created on 2018/8/9 20:51.
 *
 * @author SinKitwah
 * @Description 实现动态代理
 */
public class ProxyHelper {

    private final static Map<Class<?>, Object> BEAN_MAP;
    private final static Set<Class<?>> PROXY_BEAN;
    private static Map<Class<?>, List<Class<?>>> proxyMap;

    static {
        PROXY_BEAN = ClassHelper.getProxyClass();
        BEAN_MAP = BeanContainer.getBeanMap();
    }

    /**
     * 获取被代理类与增强类的对应关系
     * @return map集合
     */
    private static Map<Class<?>, List<Class<?>>> getProxyMap() {
        Map<Class<?>, List<Class<?>>> map = new HashMap<Class<?>, List<Class<?>>>();
        for (Class<?> cls : PROXY_BEAN) {
            //获取被代理的类
            Proxy proxy = cls.getAnnotation(Proxy.class);
            Class<?> value = proxy.value();
            //建立map保存（被代理类-->List<增强类>）
            if (map.containsKey(value)) {
                map.get(value).add(cls);
            } else {
                List<Class<?>> proxyList = new ArrayList<Class<?>>();
                proxyList.add(cls);
                map.put(value, proxyList);
            }
        }
        return map;
    }

    /**
     * 获取代理后的对象
     * @param target 被代理的类
     * @param list 增强类集合
     * @return 代理后的对象
     */
    private static Object getProxyInstance(Class<?> target, List<Class<?>> list) {
        ProxyUtil handler = new ProxyUtil(list);
        return handler.getProxy(target);
    }

    /**
     * 获取代理后的Bean容器
     * @return Bean容器
     */
    public static Map<Class<?>, Object> getProxyInstances() {
        proxyMap = getProxyMap();
        for (Map.Entry<Class<?>, List<Class<?>>> entry : proxyMap.entrySet()) {
            Class<?> target = entry.getKey();
            List<Class<?>> proxyList = entry.getValue();
            Object result = getProxyInstance(target, proxyList);
            BEAN_MAP.put(target, result);
        }
        return BEAN_MAP;
    }
}

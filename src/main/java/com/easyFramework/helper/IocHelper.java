package com.easyFramework.helper;

import com.easyFramework.util.ReflectionUtil;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Created on 2018/8/9 0:00.
 *
 * @author SinKitwah
 * @Description 实现依赖注入
 */
public final class IocHelper {

    private static final Map<Class<?>, Object> BEAN_MAP;

    /**
     * 加载类并实现依赖注入
     * 注意：暂不支持通过接口注入相应的实例
     */
    static {
        //此处使用代理后的Bean容器
        BEAN_MAP = ProxyHelper.getProxyInstances();
        //依赖注入
        if (MapUtils.isNotEmpty(BEAN_MAP)){
            //遍历BEAN_MAP
            for (Map.Entry<Class<?>, Object> beanEntry : BEAN_MAP.entrySet()){
                //从BEAN_MAP中获取bean类与bean实例
                Class<?> beanClass = beanEntry.getKey();
                Object beanInstance = beanEntry.getValue();
                //获取bean类定义的所有成员变量（bean Field）
                Field[] beanFields = beanClass.getDeclaredFields();
                if (ArrayUtils.isNotEmpty(beanFields)){
                    //遍历bean Field
                    for (Field beanField : beanFields){
                        //在BEAN_MAP中获取bean Field对应的实例
                        Class<?> beanFieldClass = beanField.getType();
                        Object beanFieldInstance = BEAN_MAP.get(beanFieldClass);
                        if (beanFieldInstance!=null){
                            //通过反射初始化beanField的值
                            ReflectionUtil.setField(beanInstance,beanField,beanFieldInstance);
                        }
                    }
                }
            }
        }
    }

    /**
     * 通过class获取实例
     * @param cls 类class
     * @param <T>
     * @return 实例
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(Class<T> cls){
        if (!BEAN_MAP.containsKey(cls)){
            throw new RuntimeException("IocHelper.getBean: can not get bean class: " + cls);
        }
        return (T) BEAN_MAP.get(cls);
    }
}

package com.easyFramework.annotation;

import java.lang.annotation.*;

/**
 * Created on 2018/8/9 20:48.
 *
 * @author SinKitwah
 * @Description 代理切面注解
 */
//可以在类上使用此注解
@Target(ElementType.TYPE)
//注解可以保留到程序运行的时候，它会被加载进入到 JVM 中，在程序运行时可以获取到它们
@Retention(RetentionPolicy.RUNTIME)
//将注解中的元素包含到 Javadoc 中去
@Documented
public @interface Proxy {
    /**
     * 被代理的类
     */
    Class<?> value();
}

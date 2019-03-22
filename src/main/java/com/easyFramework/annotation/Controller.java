package com.easyFramework.annotation;

import java.lang.annotation.*;

/**
 * Created on 2018/8/8 21:38.
 *
 * @author SinKitwah
 * @Description 注解Controller层的类
 */
//可以在类上使用Controller注解
@Target(ElementType.TYPE)
//注解可以保留到程序运行的时候，它会被加载进入到 JVM 中，在程序运行时可以获取到它们
@Retention(RetentionPolicy.RUNTIME)
//将注解中的元素包含到 Javadoc 中去
@Documented
public @interface Controller {
    //Controller不带任何元素
}

package com.easyFramework.annotation;

import java.lang.annotation.*;

/**
 * Created on 2018/8/8 21:51.
 *
 * @author SinKitwah
 * @Description IoC自动注入
 */
//可以给属性进行注解
@Target(ElementType.FIELD)
//注解可以保留到程序运行的时候，它会被加载进入到 JVM 中，在程序运行时可以获取到它们
@Retention(RetentionPolicy.RUNTIME)
//将注解中的元素包含到 Javadoc 中去
@Documented
public @interface Autowired {
}

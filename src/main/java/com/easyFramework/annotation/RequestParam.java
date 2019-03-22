package com.easyFramework.annotation;

import java.lang.annotation.*;

/**
 * Created on 2018/8/8 21:48.
 *
 * @author SinKitwah
 * @Description 获取request参数
 */
//可以在方法内的参数进行注解上使用RequestParam注解
@Target(ElementType.PARAMETER)
//注解可以保留到程序运行的时候，它会被加载进入到 JVM 中，在程序运行时可以获取到它们
@Retention(RetentionPolicy.RUNTIME)
//将注解中的元素包含到 Javadoc 中去
@Documented
public @interface RequestParam {
    //获取request属性值的名称
    String value() default "";
}

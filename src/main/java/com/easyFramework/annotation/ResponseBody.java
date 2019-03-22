package com.easyFramework.annotation;

import java.lang.annotation.*;

/**
 * Created on 2018/8/8 21:53.
 *
 * @author SinKitwah
 * @Description 被注解的方法返回JSON格式
 */
//可以在类或者方法上使用RequestMapping注解
@Target({ElementType.METHOD})
//注解可以保留到程序运行的时候，它会被加载进入到 JVM 中，在程序运行时可以获取到它们
@Retention(RetentionPolicy.RUNTIME)
//将注解中的元素包含到 Javadoc 中去
@Documented
public @interface ResponseBody {
}

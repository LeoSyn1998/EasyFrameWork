package com.easyFramework.bean;

/**
 * Created on 2018/8/9 22:48.
 *
 * @author SinKitwah
 * @Description 实现代理的切面模板
 */
public abstract class AspectProxy {
    /**
     * 前置通知
     */
    public void before(){}
    /**
     * 后置通知
     */
    public void after(){}
    /**
     * 异常通知
     */
    public void afterThrowing(){}
    /**
     * 返回后置通知
     */
    public void afterReturning(){}
}

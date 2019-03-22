package com.easyFramework.bean;

import java.util.Map;

/**
 * Created on 2018/8/9 10:17.
 *
 * @author SinKitwah
 * @Description 请求参数对象（封装的Map<String,Object>）
 */
public class Param {
    private Map<String,Object> paramMap;


    public Param(Map<String, Object> paramMap) {

        this.paramMap = paramMap;
    }

    /**
     * 根据参数名获取参数
     * @param name 参数名
     * @return Object
     */
    public Object getString(String name){
        return paramMap.get(name);
    }

    /**
     * 获取所有字段信息
     * @return
     */
    public Map<String, Object> getParamMap() {
        return paramMap;
    }
}

package com.easyFramework.bean;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Created on 2018/8/9 0:28.
 *
 * @author SinKitwah
 * @Description 封装请求信息
 */
public class Request {

    /**
     * 请求方法
     */
    private String requestMethod;

    /**
     * 请求路径
     */
    private String requestPath;

    public Request(String requestMethod, String requestPath) {
        this.requestMethod = requestMethod;
        this.requestPath = requestPath;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public String getRequestPath() {
        return requestPath;
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this,obj);
    }
}

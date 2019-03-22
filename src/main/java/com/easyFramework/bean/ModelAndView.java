package com.easyFramework.bean;

import java.util.HashMap;
import java.util.Map;

/**
 * Created on 2018/8/9 10:23.
 *
 * @author SinKitwah
 * @Description 返回视图对象
 */
public class ModelAndView {

    /**
     * 视图路径
     */
    private String path;

    /**
     * 模型数据
     */
    private Map<String, Object> model;

    public ModelAndView() {
        this.model = new HashMap<String, Object>();
    }

    public ModelAndView(String path) {
        this.path = path;
        this.model = new HashMap<String, Object>();
    }

    /**
     * 添加数据
     * @param key 键值
     * @param value 数据
     * @return 完成后的视图对象
     */
    public ModelAndView addModel(String key, Object value){
        model.put(key,value);
        return this;
    }

    public String getPath() {
        return path;
    }

    public Map<String, Object> getModel() {
        return model;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setModel(Map<String, Object> model) {
        this.model = model;
    }
}

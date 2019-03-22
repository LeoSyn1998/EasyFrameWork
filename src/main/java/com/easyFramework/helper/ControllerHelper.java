package com.easyFramework.helper;

import com.easyFramework.annotation.RequestMapping;
import com.easyFramework.bean.Handler;
import com.easyFramework.bean.Request;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;


import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created on 2018/8/9 0:42.
 *
 * @author SinKitwah
 * @Description 控制器助手类
 */
public class ControllerHelper {

    /**
     * 用于存放请求与处理方法的映射关系（Action Map）
     */
    private static final Map<Request, Handler> ACTION_MAP = new HashMap<Request, Handler>();

    static {
        //获取所有Controller类
        Set<Class<?>> controllerClassSet = ClassHelper.getControllerClass();
        if (CollectionUtils.isNotEmpty(controllerClassSet)){
            //遍历这些Controller类
            for (Class<?> controllerClass : controllerClassSet){
                String controllerMapping = "";
                //判断controller是否被RequestMapping注解
                if (controllerClass.isAnnotationPresent(RequestMapping.class)){
                    controllerMapping =
                            controllerClass.getAnnotation(RequestMapping.class).value().trim();
                }
                //获取Controller类中定义的方法
                Method[] methods = controllerClass.getDeclaredMethods();
                if (ArrayUtils.isNotEmpty(methods)){
                    //遍历这些方法
                    for (Method method : methods){
                        //判断方法是否被RequestMapping注解
                        if (method.isAnnotationPresent(RequestMapping.class)){
                            //获取方法的映射规则
                            RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                            String requestMethod = requestMapping.method();
                            String requestPath = controllerMapping + requestMapping.value().trim();
                            Request request = new Request(requestMethod,requestPath);
                            Handler handler = new Handler(controllerClass,method);
                            //初始化Action Map
                            ACTION_MAP.put(request,handler);
                        }
                    }
                }
            }
        }
    }

    /**
     * 获取Handler
     * @param requestMethod 请求方法
     * @param requestPath 请求路径
     * @return
     */
    public static Handler getHandler(String requestMethod,String requestPath){
        Request request = new Request(requestMethod,requestPath);
        return ACTION_MAP.get(request);
    }
}

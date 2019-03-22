package com.easyFramework.util;

import com.easyFramework.annotation.RequestParam;
import com.easyFramework.bean.MultiFile;
import com.easyFramework.bean.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

/**
 * Created on 2018/8/9 14:47.
 *
 * @author SinKitwah
 * @Description 接收的request数据转换成实体
 */
public class ParamUtil {

    //日志
    private static final Logger LOGGER = LoggerFactory.getLogger(ParamUtil.class);

    /**
     * 基本数据类型--包装类
     */
    static Set<Class<?>> classSet = new HashSet<Class<?>>();

    /**
     * 基本数据类型
     */
    static Set<Class<?>> classSet2 = new HashSet<Class<?>>();

    static {
        classSet.add(Byte.class);
        classSet.add(Character.class);
        classSet.add(String.class);
        classSet.add(Short.class);
        classSet.add(Integer.class);
        classSet.add(Long.class);
        classSet.add(Float.class);
        classSet.add(Double.class);
        classSet2.add(int.class);
        classSet2.add(byte.class);
        classSet2.add(char.class);
        classSet2.add(short.class);
        classSet2.add(long.class);
        classSet2.add(float.class);
        classSet2.add(double.class);
    }

    /**
     * 获取所有请求的参数
     * @param req 请求体
     * @return 参数
     */
    public static Param getParameter(HttpServletRequest req){
        try {
            Map<String, Object> paramMap = new HashMap<String,Object>();
            Map<String, String[]> params = req.getParameterMap();
            for (String key : params.keySet()) {
                String[] values = params.get(key);
                for (int i = 0; i < values.length; i++) {
                    String value = values[i];
                    paramMap.put(key, value);
                }
            }
            Param param = new Param(paramMap);
            return param;
        }catch (Exception e){
            LOGGER.error("ParamUtils.getParameter:could not get parameters form request",e);
            throw new RuntimeException(e);
        }
    }


    /**
     * 根据方法对获取的请求参数进行排序
     * @param method
     * @param requestParams
     * @param request
     * @param response
     * @return
     */
    public static Object[] getMethodArgs(Method method,Param requestParams, HttpServletRequest request, HttpServletResponse response){
        try {
            //获取方法的所有参数及类型
            Parameter[] methodParams = method.getParameters();
            Class<?>[] methodParamTypes = method.getParameterTypes();
            //需要的参数的长度
            int size = methodParams.length;
            //新建参数数组
            Object[] args = new Object[size];
            for (int i=0; i<size;i++){
                Parameter methodParam = methodParams[i];
                Class<?> methodParamType = methodParamTypes[i];
                if (methodParamType.isAssignableFrom(Param.class)){
                    args[i] = methodParam;
                }else if(methodParamType.isAssignableFrom(HttpServletRequest.class)){
                    args[i] = request;
                }else if (methodParamType.isAssignableFrom(HttpServletResponse.class)) {
                    args[i] = response;
                }else if (methodParamType.isAssignableFrom(HttpSession.class)) {
                    args[i] = request.getSession();
                }else{
                    //此处返回的是POJO或者是基本数据类型
                    //先获取参数名--是通过注解还是没有注解
                    String paramName;
                    if (methodParam.isAnnotationPresent(RequestParam.class)){
                        RequestParam requestParam = methodParam.getAnnotation(RequestParam.class);
                        paramName = requestParam.value();
                    }else{
                        //TODO 方法中的参数名被修改了，不知道是不是编译器或者是设置问题
                        paramName = methodParam.getName();
                    }
                    //生成对应的对象
                    if (methodParamType.isAssignableFrom(MultiFile.class)){
                        args[i] = ((List<MultiFile>)requestParams.getString(paramName)).get(0);
                    } else if (methodParamType.isAssignableFrom(MultiFile[].class)) {
                        List<MultiFile> multiFileList = (List<MultiFile>)requestParams.getString(paramName);
                        int listSize = multiFileList.size();
                        MultiFile[] multiFiles = new MultiFile[listSize];
                        for (int n=0;n<listSize;n++){
                            multiFiles[n]=multiFileList.get(n);
                        }
                        args[i] = multiFiles;
                    }else if(classSet.contains(methodParamType)||classSet2.contains(methodParamType)){
                        //如果这是基本数据类型--包装类
                        Object basicInstance = getBasicInstanceByString(methodParamType,(String)requestParams.getString(paramName));
                        args[i] = basicInstance;
                    }else {
                        //如果是pojo类型
                        Object pojoInstance = getPojoInstance(methodParamType,requestParams.getParamMap());
                        args[i] = pojoInstance;
                    }
                }
            }
            return args;
        }catch (Exception e){
            LOGGER.error("ParamUtils.getMethodArgs:could not sort params by method parameterTypes",e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 根据获取的String类型的数据生成基本数据对象
     * @param cls 基本类型数据对象
     * @param value String类型数据
     * @return 基本类型数据对象
     */
    public static Object getBasicInstanceByString(Class<?> cls,String value){
       try {
           if (!cls.equals(String.class) && (value == null || "".equals(value))){
               throw new RuntimeException("convert value is null");
           }
           if (classSet.contains(cls)){
               Object instance;
               try {
                   Constructor constructor = cls.getConstructor(String.class);
                   instance = constructor.newInstance(value);
               }catch (Exception e){
                   LOGGER.error("new basic instance error ",e);
                   throw new RuntimeException(e);
               }
               return instance;
           }else {
               Object data;
               if (cls.equals(int.class)) {
                   data = Integer.parseInt(value);
               } else if (cls.equals(float.class)) {
                   data = Float.parseFloat(value);
               } else if (cls.equals(byte.class)) {
                   data = Byte.parseByte(value);
               } else if (cls.equals(char.class)) {
                   data = value.toCharArray()[0];
               } else if (cls.equals(long.class)) {
                   data = Long.parseLong(value);
               } else if (cls.equals(double.class)) {
                   data = Double.parseDouble(value);
               } else {
                   data = Short.parseShort(value);
               }
               return data;
           }
       }catch (Exception e){
            LOGGER.error("ParamUtils.getBasicInstance:could not get basic date instance",e);
            throw new RuntimeException(e);
        }
    }



    /**
     * 获取pojo类型对象并赋值
     * 注意：pojo类中的属性必须都是基本数据类型，并且同时收两个或以上的pojo类型时，不可以有重名的属性名
     * @param cls 类名
     * @param fieldData 数据
     * @return
     */
    public static Object getPojoInstance(Class<?> cls,Map<String,Object> fieldData){
        try {
            Object instance = ReflectionUtil.newInstance(cls);
            //获取所有方法
            Method[] methods = cls.getMethods();
            for (Method method : methods){
                //获取方法名
                String methodName = method.getName();
                //取得setxxx或是isxxx的方法
                if (methodName.startsWith("set")||methodName.startsWith("is")){
                    if(methodName.startsWith("set")){
                        methodName = methodName.replace(methodName.substring(0,4),methodName.substring(3,4).toLowerCase());
                    }else {
                        methodName = methodName.replace(methodName.substring(0,3),methodName.substring(2,3).toLowerCase());
                    }
                    //获取该方法的参数类型
                    Class<?>[] paramTypes = method.getParameterTypes();
                    Object param = getBasicInstanceByString(paramTypes[0], (String)fieldData.get(methodName));
                    ReflectionUtil.invokeMethod(instance,method,param);
                }
            }
            return instance;
        }catch (Exception e){
            LOGGER.error("ParamUtils.getPojoInstance:could not get Pojo instance",e);
            throw new RuntimeException(e);
        }
    }

}

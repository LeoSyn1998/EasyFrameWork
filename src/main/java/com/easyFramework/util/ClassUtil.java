package com.easyFramework.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created on 2018/8/8 22:03.
 *
 * @author SinKitwah
 * @Description 类加载器-获取基础包下的所有类
 */
public class ClassUtil {

    //创建ClassUtil.class的日志记录
    private static final Logger LOGGER = LoggerFactory.getLogger(ClassUtil.class);

    /**
     * 获取类加载器
     * @return 类加载器
     */
    public static ClassLoader getClassLoader(){
        return Thread.currentThread().getContextClassLoader();
    }

    /**
     * 加载类
     * @param className 需要加载的类的全名
     * @param isInitialized 是否实例化，为了提高加载类的性能，可以设置为false
     * @return 返回加载的类
     */
    public static Class<?> loadClass(String className,boolean isInitialized){
        Class<?> cls;
        try {
            cls = Class.forName(className,isInitialized,getClassLoader());
        }catch (ClassNotFoundException e){
            LOGGER.error("ClassUtil.loadClass: load class failure",e);
            throw new RuntimeException(e);
        }
        return cls;
    }

    /**
     * 获取指定包名下的所有类（不含jar文件）
     * @param packageName 指定包名
     * @return
     */
    public static Set<Class<?>> getClassSet(String packageName){
        //包名不能为空
        if (packageName == null) {
            LOGGER.error("package is null");
            throw new RuntimeException("package is null");
        }
        Set<Class<?>> classSet = new HashSet<Class<?>>();
        try{
            //包名对应的路径名称
            String packagePath = packageName.replace('.', '/');
            //获取可以查找到文件的资源定位符的迭代器
            Enumeration<URL> urls = getClassLoader().getResources(packagePath);
            //遍历基础包下所有的类
            while(urls.hasMoreElements()){
                URL url = urls.nextElement();
                if (url!=null){
                    //获取url的协议--文件类型
                    String protool = url.getProtocol();
                    if ("file".equals(protool)){
                        //文件类型的扫描
                        String filePath = URLDecoder.decode(url.getPath(),"UTF-8");
                        findAllClassByFile(classSet,filePath,packageName);
                    }else if ("jar".equals(protool)){
                        //jar文件加载
                        //此处一般是用不上的
                        //根据url读取jar文件
                        /*JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
                        if (jarURLConnection != null){
                            //获取jarFile对象
                            JarFile jarFile = jarURLConnection.getJarFile();
                            if (jarFile != null){
                                //遍历jar文件
                                Enumeration<JarEntry> jarEntries = jarFile.entries();
                                while (jarEntries.hasMoreElements()){
                                    JarEntry jarEntry = jarEntries.nextElement();
                                    String jarEntryName = jarEntry.getName();
                                    if (jarEntryName.endsWith(".class")){
                                        String className =
                                                jarEntryName.substring(0,jarEntryName.lastIndexOf(".")).replaceAll("/",".");
                                        classSet.add(loadClass(className,false));
                                    }
                                }
                            }
                        }*/
                    }
                }
            }
        }catch (Exception e){
            LOGGER.error("ClassUtil.getClassSet: could not get class from basic path",e);
            throw new RuntimeException(e);
        }
        return classSet;
    }

    /**
     * 遍历基础包所有的文件夹，加载所有class文件
     * @param classSet  用来保存类的
     * @param filePath 文件夹的路径
     * @param packageName 包名
     */
    private static void findAllClassByFile(Set<Class<?>> classSet,String filePath,String packageName){
        //加载此文件夹下的文件夹或者是class文件
        File[] files = new File(filePath).listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                // 接受dir目录(子文件夹)
                boolean acceptDir = file.isDirectory();
                // 接受class文件
                boolean acceptClass = file.getName().endsWith("class");
                return acceptDir || acceptClass;
            }
        });
        for (File file : files){
            //获取文件名
            String fileName = file.getName();
            if(file.isDirectory()){ //判断这是不是文件夹
                //子包名
                String subPackageName = fileName;
                if (StringUtils.isNotEmpty(packageName)){
                    subPackageName = packageName + "." + subPackageName;
                }
                //子包对应的路径的名称
                String subFilePath = file.getAbsolutePath();
                //递归调用访问所有的文件夹
                findAllClassByFile(classSet,subFilePath,subPackageName);
            }else { //这是class文件则直接加载
                //转换成类名
                String className = packageName + "." + fileName.replace(".class", "").trim();
                //加载类并放到classSet集合
                classSet.add(loadClass(className,false));
            }
        }
    }

}

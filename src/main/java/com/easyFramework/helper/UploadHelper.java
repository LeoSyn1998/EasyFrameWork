package com.easyFramework.helper;

import com.easyFramework.bean.MultiFile;
import com.easyFramework.bean.Param;
import com.easyFramework.util.PropertiesUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created on 2018/8/11 14:24.
 *
 * @author SinKitwah
 * @Description 文件上传类助手
 */
public class UploadHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(UploadHelper.class);

    /**
     * Apache Commons FileUpload 提供的Servlet文件上传对象
     */
    private static ServletFileUpload servletFileUpload;

    /**
     * 初始化上传文件处理器（在Servlet中注册）
     * 当配置中接收文件大小大于零时才会注册上传文件处理器
     * @param servletContext Servlet的配置环境信息
     */
    public static void init(ServletContext servletContext){
        //当接收文件大于零时才会进行注册
        int uploadLimit = PropertiesUtil.getUploadLimit();
        if (uploadLimit > 0){
            File repository = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
            servletFileUpload = new ServletFileUpload(
                    new DiskFileItemFactory(DiskFileItemFactory.DEFAULT_SIZE_THRESHOLD,repository));
            servletFileUpload.setFileSizeMax(uploadLimit*1024*1024);
        }
    }

    /**
     * 判断请求类型是否为multipart
     */
    public static boolean isMultipart(HttpServletRequest request){
        return ServletFileUpload.isMultipartContent(request);
    }

    /**
     * 获取请求参数
     * @param request
     * @return
     * @throws IOException
     */
    public static Param getParameter(HttpServletRequest request) throws IOException{
        //参数集合
        Map<String,Object> params = new HashMap<String,Object>();
        try {
            //获取文件集合映射
            Map<String,List<FileItem>> fileItemListMap = servletFileUpload.parseParameterMap(request);
            if (MapUtils.isNotEmpty(fileItemListMap)){
                for (Map.Entry<String,List<FileItem>> fileItemListEntry:fileItemListMap.entrySet()){
                    //获取请求参数名
                    String fieldName = fileItemListEntry.getKey();
                    List<FileItem> fileItemList = fileItemListEntry.getValue();
                    if (CollectionUtils.isNotEmpty(fileItemList)){
                        for (FileItem fileItem : fileItemList){
                            if(!fileItem.isFormField()){ //如果是文件
                                //获取真实文件名（去掉文件路径）
                                String fileName = FilenameUtils.getName(
                                        new String(fileItem.getName().getBytes(),"UTF-8"));
                                if (StringUtils.isNotEmpty(fieldName)){
                                    //获取文件大小
                                    long fileSize = fileItem.getSize();
                                    //获取文件类型
                                    String contentType = fileItem.getContentType();
                                    //获取文件输入流
                                    InputStream inputStream = fileItem.getInputStream();
                                    List<MultiFile> fileParamList;
                                    if (params.containsKey(fieldName)){
                                        fileParamList = (List<MultiFile>)params.get(fieldName);
                                    }else {
                                        fileParamList = new ArrayList<>();
                                    }
                                    //建立FileParam实体,并放入List集合
                                    fileParamList.add(new MultiFile(fieldName,fileName,fileSize,contentType,inputStream));
                                    params.put(fieldName,fileParamList);
                                }else{
                                    //获取普通参数
                                    String fieldValue = fileItem.getString("UTF-8");
                                    params.put(fieldName,fieldValue);
                                }
                            }
                        }
                    }
                }
            }
        }catch (FileUploadException e){
            LOGGER.error("UploadHelper.getParameter:get multiFile param from request failure",e);
            throw new RuntimeException(e);
        }
       return new Param(params);
    }

}

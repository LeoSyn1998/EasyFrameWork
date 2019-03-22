package com.easyFramework.bean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * Created on 2018/8/11 13:26.
 *
 * @author SinKitwah
 * @Description 封装上传文件参数
 */
public class MultiFile {

    private static final Logger LOGGER = LoggerFactory.getLogger(MultiFile.class);
    /**
     * 请求参数名
     */
    private String fieldName;
    /**
     * 文件名
     */
    private String fileName;
    /**
     * 文件大小
     */
    private long fileSize;
    /**
     * 上传文件的Content-Type,可判断文件的上传类型
     */
    private String contentType;
    /**
     * 文件输入流
     */
    private InputStream inputStream;

    public MultiFile(String fieldName, String fileName, long fileSize, String contentType, InputStream inputStream) {
        this.fieldName = fieldName;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.contentType = contentType;
        this.inputStream = inputStream;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getFileName() {
        return fileName;
    }

    public long getFileSize() {
        return fileSize;
    }

    public String getContentType() {
        return contentType;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    /**
     * 保存文件
     * @param file
     */
    public void transferTo(File file){
        if (file==null) return;
        try {
            //获取文件所在文件夹，如果不存在则新建
            File parentDir = file.getParentFile();
            if (!parentDir.exists()){
                parentDir.mkdir();
            }
            OutputStream outputStream = new FileOutputStream(file);
            //创建一个缓冲区
            byte buffer[] = new byte[4*1024];
            //判断输入流中的数据是否已经读完的标识
            int len = 0;
            //循环将输入流读入到缓冲区当中，(len=in.read(buffer))>0就表示in里面还有数据
            while ((len = inputStream.read(buffer)) > 0) {
                //使用FileOutputStream输出流将缓冲区的数据写入到指定的目录(savePath + "\\" + filename)当中
                outputStream.write(buffer, 0, len);
            }
            //关闭输入流
            inputStream.close();
            //关闭输出流
            outputStream.close();
        }catch (Exception e){
            LOGGER.error("MultiFile.transferTo: transfer inputStream to file failure ",e);
            throw new RuntimeException(e);
        }
    }
}

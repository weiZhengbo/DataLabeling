package com.dataLabeling.util;

import com.dataLabeling.entity.SimilarRecord;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.*;
import java.util.zip.ZipOutputStream;

public class CommonUtils
{
    /**
     * 生产uuid
     * @return
     */
    public static String uuid()
    {
        return UUID.randomUUID().toString().replace("-", "").toUpperCase();
    }

    /**
     * 将map转换为对象
     * @param map
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T toBean(Map map, Class<T> clazz)
    {
        try
        {
            Object bean = clazz.newInstance();
            ConvertUtils.register(new DateConverter(), Date.class);
            BeanUtils.populate(bean, map);
            return (T) bean;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * md5加密
     * @param str
     * @return
     * @throws Exception
     */
    public static String getMD5Str(String str) throws Exception {
        try {
            // 生成一个MD5加密计算摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 计算md5函数
            md.update(str.getBytes());
            // digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
            return new BigInteger(1, md.digest()).toString(16);
        } catch (Exception e) {
            throw new Exception("MD5加密出现错误，"+e.toString());
        }
    }

    /**
     * 判断是否是数字
     * @param str
     * @return
     */
    public static boolean isDigit(String str){
        try {
            Integer.parseInt(str);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    /**
     * 按照groupsize切分list
     * @param list
     * @param groupSize
     * @param <T>
     * @return
     */
    public   static <T> List<List<T>> splitList(List<T> list , int groupSize){
        int length = list.size();
        // 计算可以分成多少组
        int num = ( length + groupSize - 1 )/groupSize ; // TODO
        List<List<T>> newList = new ArrayList<>(num);
        for (int i = 0; i < num; i++) {
            // 开始位置
            int fromIndex = i * groupSize;
            // 结束位置
            int toIndex = (i+1) * groupSize < length ? ( i+1 ) * groupSize : length ;
            newList.add(list.subList(fromIndex,toIndex)) ;
        }
        return  newList ;
    }

    public static int getInt(String pc) {
        if(pc != null && !pc.trim().isEmpty()&& !pc.equals("null")) {
            return Integer.parseInt(pc);
        }
        return 1;
    }

    public static int getOtherParam(String pctype) {
        if(pctype != null && !pctype.trim().isEmpty()&&!pctype.equals("null")) {
            return Integer.parseInt(pctype);
        }
        return -1;
    }

    public static String getOther(String type){
        if (type!=null && !type.trim().isEmpty()&&!type.equals("null")){
            return type;
        }
        return "";
    }

    /**
     * 下载普通文件
     * @param fileName
     * @param file
     * @param response
     */
    public static void downLoadFile(String fileName,File file,HttpServletResponse response){
        OutputStream os  = null;
        try {
            fileName =  URLEncoder.encode(fileName, "UTF-8");
            os = response.getOutputStream();
            response.reset();
            response.setHeader("Content-Disposition", "attachment;filename="+fileName);
            response.setContentType("application/octet-stream; charset=utf-8");
            os.write(FileUtils.readFileToByteArray(file));
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            IOUtils.closeQuietly(os);
            file.delete();
        }
    }

    /**
     * 下载zip文件
     * @param zipName
     * @param files
     * @param response
     */
    public static void downLoadZipFile(String zipName,List<File> files,HttpServletResponse response){
        response.setHeader("Content-Disposition","attachment; filename="+zipName);
        response.setContentType("multipart/form-data");
        ZipOutputStream out = null;
        try {
            out = new ZipOutputStream(response.getOutputStream());
            for (File file:files){
                ZipUtils.doCompress(file.getPath(), out);
                response.flushBuffer();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
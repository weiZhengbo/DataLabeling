package com.dataLabeling.util;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

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
}
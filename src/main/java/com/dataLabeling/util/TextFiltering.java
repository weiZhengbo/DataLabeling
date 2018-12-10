package com.dataLabeling.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextFiltering implements Serializable {

    public static void main(String[] args) {
        String a = "360";
        System.out.println(filter(a));
    }
    //用于过滤文本
    public static String filter(String text){
        //去掉前后空格
        text=text.trim();
        //过滤全是数字
        String numPatternStr="\\d+";
        if (text.matches(numPatternStr)){
            return "";
        }
        //过滤url
        String urlPatternStr = "(https?|ftp|file)://[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]";
        text = text.replaceAll(urlPatternStr, "");
        //过无用词进行过滤 利用useless.txt文件
        String uselessPath = TextFiltering.class.getResource("/useless.txt").toString().substring(6);
        BufferedReader reader = null;
        try {
            FileInputStream fis = new FileInputStream(uselessPath);
            InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
            reader = new BufferedReader(isr);
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                text = text.replace(tempString,"");
            }
            reader.close();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
        //过滤文本开始和结束的标点
        String pattern = "^(\\*|,|，|。|\\.|\\?|？|!|！|、)+(.*)";
        String pattern1 = "(.*?)(\\*|,|，|。|\\.|\\?|？|!|！|、)+$";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(text);
        if (m.matches()){
            text=m.group(2);
        }
        p=Pattern.compile(pattern1);
        m=p.matcher(text);
        if (m.matches()){
            text=m.group(1);
        }
        //去掉html标签
        text=htmlRemoveTag(text);
        //去掉前后空格
        text=text.trim();
        //过滤全是数字
        if (text.matches(numPatternStr)){
            return "";
        }
        return text;
    }

    //去掉html标签
    public static String htmlRemoveTag(String inputString) {
        if (inputString == null)
            return null;
        String htmlStr = inputString; // 含html标签的字符串
        String textStr = "";
        Pattern p_script;
        Matcher m_script;
        Pattern p_style;
        Matcher m_style;
        Pattern p_html;
        Matcher m_html;
        String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>";
        String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>";
        String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
        p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
        m_script = p_script.matcher(htmlStr);
        htmlStr = m_script.replaceAll(""); // 过滤script标签
        p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
        m_style = p_style.matcher(htmlStr);
        htmlStr = m_style.replaceAll(""); // 过滤style标签
        p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
        m_html = p_html.matcher(htmlStr);
        htmlStr = m_html.replaceAll(""); // 过滤html标签
        textStr = htmlStr;
        return textStr;// 返回文本字符串
    }
}

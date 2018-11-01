package com.dataLabeling.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class FileReadUtil {

    public static String readTxt(File file){

        StringBuilder result = new StringBuilder();
        try{
            BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
            String s = null;
            while((s = br.readLine())!=null){//使用readLine方法，一次读一行
                result.append(s);
            }
            br.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return result.toString().trim();
    }

    public static void readExcel(File file){


    }
}

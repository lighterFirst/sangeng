package com.cheng.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class PathUtils {

    public static String generateFilePath(String fileName){
        //根据日期生成路径   2022/1/15/
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/");
        String datePath = sdf.format(new Date());
        //uuid作为文件名
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        //后缀和文件后缀一致()   返回指定字符在此字符串中最后一次出现处的索引
        int index = fileName.lastIndexOf(".");
        // test.jpg -> .jpg  substring从第几位开始截取字符串到最后（一个参数中）
        String fileType = fileName.substring(index);
        
        return new StringBuilder().append(datePath).append(uuid).append(fileType).toString();
    }

    public static void main(String[] args) {
        String s = generateFilePath("3.png");
        System.out.println(s);
    }

}

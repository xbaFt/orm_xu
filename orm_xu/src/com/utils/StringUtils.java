package com.utils;

/**
 * 封装字符串常用操作
 */
public class StringUtils {
    /**
     * 将字符串首字母大写并返回
     * @param str 目标字符串
     * @return
     */
    public static String firstChar2UpperCase(String str){
        return  str.substring(0,1).toUpperCase()+str.substring(1);
    }
}

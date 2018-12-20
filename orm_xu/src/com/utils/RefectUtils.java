package com.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 封装反射常用操作
 */
public class RefectUtils {

    /**
     * 通过反射获取字段对应的get方法
     * @param fieldName 字段名称
     */
    public static Object invokeGet(Object object,String fieldName){
        try {
            Class clzz =  object.getClass();
            Method method = clzz.getDeclaredMethod("get"+StringUtils.firstChar2UpperCase(fieldName),null);
            return method.invoke(object,null);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 反射生成set方法
     * @param object
     * @param columnName
     * @param columnValue
     */
    public static void invokeSet(Object object,String columnName,Object columnValue){
        try {
            Method m = object.getClass().getDeclaredMethod("set"+StringUtils.firstChar2UpperCase(columnName),
                    columnValue.getClass());
            m.invoke(object,columnValue);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}

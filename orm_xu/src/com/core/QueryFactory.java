package com.core;

/**
 * 根据配置信息创建query对象
 */
public class QueryFactory {
    private QueryFactory(){}
    private static Query prototype ;
    //反射加载对象
    static {
        try {
            Class clazz = Class.forName(DBMananger.getCof().getQuery());
            prototype = (Query) clazz.newInstance();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }
    public static Query createQuery() throws CloneNotSupportedException {
        return (Query) prototype.clone();
    }
}

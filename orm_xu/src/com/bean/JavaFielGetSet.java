package com.bean;

public class JavaFielGetSet {
    /**
     * 封装java字段源码及get set方法
     */
    /**
     * 属性源码 如private String username
     */
    private String fieldInfo;
    //public String getUser(){}
    private String getIfo;
    //public String setUser(String user){}
    private String setInfo;

    public JavaFielGetSet() {
    }

    public JavaFielGetSet(String fieldInfo, String getIfo, String setInfo) {
        this.fieldInfo = fieldInfo;
        this.getIfo = getIfo;
        this.setInfo = setInfo;
    }

    public String getFieldInfo() {
        return fieldInfo;
    }

    public void setFieldInfo(String fieldInfo) {
        this.fieldInfo = fieldInfo;
    }

    public String getGetIfo() {
        return getIfo;
    }

    public void setGetIfo(String getIfo) {
        this.getIfo = getIfo;
    }

    public String getSetInfo() {
        return setInfo;
    }

    public void setSetInfo(String setInfo) {
        this.setInfo = setInfo;
    }

    @Override
    public String toString() {
        return "JavaFielGetSet{\n" +
                 fieldInfo  +
                 getIfo +
                 setInfo +
                '}';
    }
}

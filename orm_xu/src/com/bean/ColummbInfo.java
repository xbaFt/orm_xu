package com.bean;

/**
 * 封装字段信息
 */
public class ColummbInfo {

    /**
     * 字段名称
     */
    private String name;

    /**
     * 字段数据类型
     */
    private String dataType;

    /**
     * 字段建类型(0:普通键,1:主键, 2:外键)
     */
    private int keyType;

    public ColummbInfo(String name, String dataType, int keyType) {
        this.name = name;
        this.dataType = dataType;
        this.keyType = keyType;
    }

    public ColummbInfo() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public int getKeyType() {
        return keyType;
    }

    public void setKeyType(int keyType) {
        this.keyType = keyType;
    }
}
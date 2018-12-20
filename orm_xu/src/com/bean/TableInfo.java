package com.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 封装表信息
 */
public class TableInfo {

    /**
     * 表名
     */
    private String name;

    /**
     * 字段信息
     */
    private Map<String,ColummbInfo> columms;

    /**
     * 唯一主键
     */
    private ColummbInfo onlyPriKey;

    //联合主键
    private List<ColummbInfo> priKeys;

    public TableInfo() {
    }

    public List<ColummbInfo> getPriKeys() {
        return priKeys;
    }

    public void setPriKeys(List<ColummbInfo> priKeys) {
        this.priKeys = priKeys;
    }



    public TableInfo(String name, Map<String,ColummbInfo> columms, List<ColummbInfo> priKeys) {
        this.name = name;
        this.columms = columms;
        this.priKeys = priKeys;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, ColummbInfo> getColumms() {
        return columms;
    }

    public void setColumms(Map<String, ColummbInfo> columms) {
        this.columms = columms;
    }

    public ColummbInfo getOnlyPriKey() {
        return onlyPriKey;
    }

    public void setOnlyPriKey(ColummbInfo onlyPriKey) {
        this.onlyPriKey = onlyPriKey;
    }
}

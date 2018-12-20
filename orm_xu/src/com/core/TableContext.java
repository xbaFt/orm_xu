package com.core;

import com.bean.ColummbInfo;
import com.bean.Configuration;
import com.bean.TableInfo;
import com.utils.JavaFileUtils;
import com.utils.StringUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 负责获取和管理数据库表结构和类结构关系
 * 可以根据表结构生成类结构
 */
public class TableContext {
    //储存表结构
    private static Map<String, TableInfo> tables = new HashMap<>();
    /**
     * 将po的class对象和表信息对象关联起来
     */
    public static Map<Class,TableInfo> poClassTableMap = new HashMap<>();

    public TableContext() {
    }
    //初始化获得表信息
    static{
        Connection con = DBMananger.getConn();
        try {
            DatabaseMetaData dbmd = con.getMetaData();
            ResultSet tableRet = dbmd.getTables(null,"%","%",new String[]{"TABLE"});
            while (tableRet.next()){
                String tableName = (String) tableRet.getObject("TABLE_NAME");
                TableInfo ti = new TableInfo(tableName,new HashMap<>(),new ArrayList<>());
                tables.put(tableName,ti);
                //查询表中所有字段
                ResultSet set = dbmd.getColumns(null,"%",tableName,"%");
                //把字段信息封装到Tableinfo的columms变量中
                while (set.next()){
                    ColummbInfo ci = new ColummbInfo(set.getString("COLUMN_NAME"),set.getString("TYPE_NAME"),0);
                    ti.getColumms().put(set.getString("COLUMN_NAME"),ci);
                }
                //获取所有主键信息
                ResultSet keySet = dbmd.getPrimaryKeys(null,"%",tableName);
                //遍历封装数据
                while (keySet.next()){
                    ColummbInfo ci2 =ti.getColumms().get(keySet.getString("COLUMN_NAME"));

                    //设置为主键类型
                    ci2.setKeyType(1);
                    ti.getPriKeys().add(ci2);
                }

                //取出唯一主键，如果是联合主键则为空
                if(ti.getPriKeys().size()>0){
                    ti.setOnlyPriKey(ti.getPriKeys().get(0));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    public static Map<String,TableInfo> getTables(){
        return tables;
    }

    /**
     * 根据数据库中的表 更新po包的java文件
     */
    public static void updateJavaPoFile(){
        Map<String,TableInfo> map = TableContext.tables;
        for (TableInfo tableInfo : map.values()){
            JavaFileUtils.createJavaPoFile(tableInfo,new MysqlTypeConvertor());
        }
    }

    /**
     * 加载po包下面的类,并存放到poClassTableMap中
     */
    public static void loadClassPo(){
        try {
            for (TableInfo tableInfo:tables.values()){
                Class clzz = Class.forName(DBMananger.getCof().getPoPackage()+"."+ StringUtils.firstChar2UpperCase(tableInfo.getName()));
                poClassTableMap.put(clzz,tableInfo);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}

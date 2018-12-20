package com.core;

import com.bean.Configuration;
import com.pool.DBConnPool;
import com.sun.org.apache.xerces.internal.util.EntityResolverWrapper;

import java.io.IOException;
import java.sql.*;
import java.util.Properties;

/**
 * 根据配置信息，管理链接对象 增加链接池功能)
 */
public class DBMananger {
    private static DBConnPool pool;
    private static Configuration cof;
    //类初始化加载配置文件
    static {
        Properties properties = new Properties();
        //加载配置文件
        try {
            properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("db.propertiest"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //构建配置对象
        cof = new Configuration();
        cof.setDirver(properties.getProperty("dirver"));
        cof.setUser(properties.getProperty("user"));
        cof.setPwd(properties.getProperty("pwd"));
        cof.setPoPackage(properties.getProperty("poPackage"));
        cof.setUrl(properties.getProperty("url"));
        cof.setUsingDB(properties.getProperty("usingDB"));
        cof.setSrcPath(properties.getProperty("srcPath"));
        cof.setQuery(properties.getProperty("queryClass"));
        cof.setPoolMax(Integer.parseInt(properties.getProperty("poolMax")));
        cof.setPoolMin(Integer.parseInt(properties.getProperty("poolMin")));

        System.out.println(TableContext.class);
    }
    /**
     * 获取链接对象
     */

    public static Connection getConn(){
        if (pool==null){
            pool = new DBConnPool();
        }
        return  pool.getConn();
    }

    public static Connection createConn(){
        try {
            Class.forName(cof.getDirver());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            return DriverManager.getConnection(cof.getUrl(),cof.getUser(),cof.getPwd());
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 关闭链接
     * @param resultSet
     * @param statement
     * @param connection
     */
    public static void close(ResultSet resultSet,Statement statement,Connection connection) throws SQLException {
        if (resultSet!=null){
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }if (statement!=null){
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (connection!=null){
            pool.close(connection);
        }
    }

    /**
     * 关闭链接
     * @param statement
     * @param con
     */
    public static void close(Statement statement,Connection con) throws SQLException {
        if (statement!=null){
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (con!=null){
           pool.close(con);
        }
    }

    /**
     * 获取配置信息
     * @return
     */
    public static Configuration getCof(){
        return cof;
    }
}

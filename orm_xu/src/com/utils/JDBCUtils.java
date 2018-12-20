package com.utils;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 封装数据库操作
 */
public class JDBCUtils {

    /**
     * 给sql语句传递参数
     * @param preparedStatement 预编译的sql对象
     * @param params 参数列表
     * @throws SQLException
     */
    public static void handleParams(PreparedStatement preparedStatement,Object[] params) throws SQLException {
        if(preparedStatement!=null){
            for (int i = 0;i < params.length; i++){
                preparedStatement.setObject(1+i,params[i]);
            }
        }
    }
}

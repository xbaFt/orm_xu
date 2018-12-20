package com.pool;

import com.bean.Configuration;
import com.core.DBMananger;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据库链接池
 */
public class DBConnPool {
    private List<Connection> pool;
    private static final int P00L_MAX = DBMananger.getCof().getPoolMax();
    private static final int POOL_MIN = DBMananger.getCof().getPoolMin();

    public DBConnPool(){
            init();
    }
    /**
     * 初始化链接池
     */
    public void init(){
        if (pool==null){
            pool = new ArrayList<Connection>();
        }
        while (pool.size()<DBConnPool.POOL_MIN){
            pool.add(DBMananger.createConn());
            System.out.println("正在初始化链接池,池中链接数量"+pool.size());
        }
    }

    /**
     * 取出链接池中最后一个链接
     */
    public synchronized Connection getConn(){
        int index_last = pool.size()-1;
        Connection con = pool.get(index_last);
        pool.remove(index_last);
        return con;
    }

    /**
     * 将链接放回链接池
     */
    public synchronized void close(Connection conn) throws SQLException {
        //判断链接池是否已存满,是则把链接关闭 不放回池中
        if (conn!=null&&pool.size()>=DBConnPool.P00L_MAX){
            conn.close();
        }
        else{
            pool.add(conn);
        }
    }
}

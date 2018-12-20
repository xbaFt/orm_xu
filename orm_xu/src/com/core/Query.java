package com.core;

import com.bean.ColummbInfo;
import com.bean.TableInfo;
import com.utils.JDBCUtils;
import com.utils.RefectUtils;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 对外提供一个接口 进行增删改查
 */
public abstract class Query implements Cloneable {
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    /**
     *查询模板
     * @param sql
     * @param clzz
     * @param params
     * @param back
     * @return
     */
    public Object executeQureyTemplate(String sql, Class clzz, Object[] params, CallBack back){
        Connection con = DBMananger.getConn();
        ResultSet res = null;
        PreparedStatement ps =null;
        try {
            ps = con.prepareStatement(sql);
            JDBCUtils.handleParams(ps,params);
            System.out.println(ps);
            res = ps.executeQuery();
            return back.doExecute(con,ps,res);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                DBMananger.close(res,ps,con);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 提交执行sql语句
     * @param sql sql语句
     * @param params 参数列表
     * @return 受影响行数
     */
    public int executeDML(String sql,Object[] params){
        Connection conn = DBMananger.getConn();
        PreparedStatement ps = null;
        int count = 0;
        try {
            ps = conn.prepareStatement(sql);
            System.out.println(sql);
            JDBCUtils.handleParams(ps,params);
            count = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                DBMananger.close(ps,conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return count;
    }

    /**
     * 把一个对象数据更新到数据库中
     * @param object 要进行更新的对象
     */
    public void insert(Object object){
        Class cl = object.getClass();
        TableInfo tableInfo = TableContext.poClassTableMap.get(cl);
        //获取所有字段 将不为空的字段添加到数据库中
        Field[] fs = cl.getDeclaredFields();
        StringBuilder sql = new StringBuilder("insert into "+tableInfo.getName()+" (");
        //参数列表
        List<Object> paramslist = new ArrayList<>();
        int countNotNullFiedl = 0;
        for (Field f : fs){
            String fieldName = f.getName();
            Object fieldValue = RefectUtils.invokeGet(object,fieldName);
            if (fieldValue!=null){
                countNotNullFiedl++;
                sql.append(fieldName+",");
                paramslist.add(fieldValue);
            }
        }
        sql.setCharAt(sql.length()-1,')');
        sql.append(" values (");
        for (int i = 0; i<countNotNullFiedl;i++){
            sql.append("?,");
        }
        sql.setCharAt(sql.length()-1,')');
        executeDML(sql.toString(),paramslist.toArray());
    }

    /**
     *删除类clzz在数据中所映射的表,以id为条件
     * @param clzz 表结构所映射的类对象
     * @param id 主键值
     */
    public void delete(Class clzz,Object id){
        TableInfo tableInfo = TableContext.poClassTableMap.get(clzz);
        //获取主键根据主键删除
        ColummbInfo onlyPriKey = tableInfo.getOnlyPriKey();
        String delsql = "delete from "+ tableInfo.getName() + " where "+ onlyPriKey.getName()+"=?";
        executeDML(delsql, new Object[]{id});
    }

    /**
     * 删除对象所对应记录 对象值对应到记录
     * @param clzz
     */
    public void delete(Object clzz){
        Class c = clzz.getClass();
        TableInfo tableInfo = TableContext.poClassTableMap.get(c);
        ColummbInfo onlyPriKey = tableInfo.getOnlyPriKey();
        //通过反射拿到要删除的索引值
        Method m = null;
        Object priKeyValue = RefectUtils.invokeGet(clzz,onlyPriKey.getName());
        delete(c,priKeyValue);
    }

    /**
     *更新对象对应记录,并且只更新指定属性
     * @param object 记录映射的对象
     * @param fileName 属性数组
     * @return 影响行数
     */
    public int update(Object object,String[] fileName){
        //存放参数列表
        List<Object> params = new ArrayList<>();
        Class cl = object.getClass();
        TableInfo tableInfo = TableContext.poClassTableMap.get(cl);
        if (tableInfo.getName()!=null) System.out.println(tableInfo.getName());
        StringBuilder sqlup = new StringBuilder("update "+tableInfo.getName()+" set ");
        //获取主键
        ColummbInfo prikey = tableInfo.getOnlyPriKey();
        for (String fname:fileName){
            Object fiedlValue = RefectUtils.invokeGet(object,fname);
            params.add(fiedlValue);
            sqlup.append(fname +"=? ");
            sqlup.setCharAt(sqlup.length()-1,' ');
        }
        sqlup.append(" where ");
        sqlup.append(prikey.getName()+" = ?");
        params.add(RefectUtils.invokeGet(object,prikey.getName()));
        return executeDML(sqlup.toString(),params.toArray());
    }

    /**
     * 查询并返回多行记录,并返回一个集合,并将每行记录封装到指定的clzz的指定类中
     * @param sql 执行的sql语句
     * @param clazz 封装数据的JavaBean类的Class对象
     * @param params sql参数
     * @return 结果列表
     */
    public List queryRows(String sql,Class clazz,Object[] params){
        return (List)executeQureyTemplate(sql, clazz, params, new CallBack() {
            @Override
            public Object doExecute(Connection conn, PreparedStatement ps, ResultSet rs) throws SQLException {
                ResultSetMetaData rsmd = rs.getMetaData();
                List list = null;
                while (rs.next()) {
                    if (list==null) list= new ArrayList();
                    try {
                        Object row = clazz.newInstance();
                        for (int i = 0; i < rsmd.getColumnCount(); i++) {
                            String columnName = rsmd.getColumnName(i + 1);
                            Object columnValue = rs.getObject(i + 1);
                            //反射拿到set方法注入属性值
                            RefectUtils.invokeSet(row, columnName, columnValue);
                        }
                        list.add(row);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    }
                }
                return list;
            }
        });
    }

    /**
     * 查询返回一行记录
     * @param sql 要执行的sql语句
     * @param clazz 封装查询数据的javaBean类的class对象
     * @param params sql参数
     * @return 返回查询到对象
     */
    public Object queryUinqueRow(String sql,Class clazz,Object[] params){
        List list = queryRows(sql,clazz,params);
        return (list==null)?null:list.get(0);
    }

    /**
     * 查询返回一个结果
     * @param sql 要执行的sql语句
     * @param parames 参数列表
     * @return 查询结果
     */
    public Object queryValue(String sql,Object[] parames){
        return executeQureyTemplate(sql, null, parames, new CallBack() {
            @Override
            public Object doExecute(Connection conn, PreparedStatement ps, ResultSet rs) throws SQLException {
                Object o = null;
                while (rs.next()){
                    o = rs.getObject(1);
                }
                return o;
            }
        });
    }

    /**
     * 查询返回一个数字
     * @param sql 执行的sql语句
     * @param parames 参数列表
     * @return 数字
     */
    public Number queryNumber(String sql,Object[] parames){
        return (Number)queryValue(sql,parames);
    }

    /**
     * 分页函数
     * @param pageNum
     * @param size
     * @return
     */
    public abstract Object queryPagenate(int pageNum,int size);
}

/**
 * 负责对mysql数据库进行操作
 */
class MysqlQueqry extends Query  {

    /*public static void main(String[] args) throws CloneNotSupportedException {
        Long start = System.currentTimeMillis();
        for (int i=0;i<1000;i++){
            test();
        }
        Long end = System.currentTimeMillis();
        System.out.println((end-start));
    }*/

    @Override
    public Object queryPagenate(int pageNum, int size) {
        return null;
    }
    public static void test() throws CloneNotSupportedException {
        Object o = QueryFactory.createQuery().queryValue("select count(*) from emp where salary > ?",new Object[]{5000});
        System.out.println(o);
    }
}
项目结构:
    bean包:
        ColummbInfo:封装数据库中表的字段信息
            name:字段名称
            dataType:字段类型
            keyType:字段键类型(0:普通键，1:主键，2:外键)
        
        Configuration:封装配置信息
            dirver:驱动
            url:数据链接
            user:用户
            pwd:密码
            usingDB:数据库类型
            srcPath:逆向生成的java源码的路径
            poPackage:逆向生成的java源码所在包
        
        JavaFielGetSet:封装java字段源码及get，set源码
            fieldInfo:字段源码
            getInfo:get方法源码
            setInfo:set方法源码

        TableInfo:封装表信息
            name:表名
            columms:字段名称map<String,ColummbInfo>
            onlyPriKey:唯一主键 ColummInfo
            priKey:联合主键List<ColummbInfo>
        
    core:
        DBMananger:根据配置信息,管理链接对象
            cof:配置信息对象
            pool:数据库链接池对象
            
            -getConn():从链接池中获取链接对象
            -createConn():直接获取对象
            -close(ResultSet,Statement,Connection):ResultSet,Statement对象,把链接对象放回链接池
            -close(Statement,Connection):同上
            -getCof():返回配置对象

            实现思路:
                1 DBMananger加载时就在静态代码块中加载初始化配置文件,并用配置文件中的属性值初始化配置对象即cof
                2 数据库链接池对象象调用createConn()方法,往链接池里放入对象,其他对象通过调用getConn()方法获取链接池里的链接对象
                3 close() 把不用的链接对象放回链接池
        
        MysqlTypeConvertor:负责数据类型之间的转换,实现TypeConvertor接口
                databaseType2JavaType():把数据库数据类型转换成java数据类型
                javadataType2dataType():把java数据类型转换成mysql数据类型
        Query:抽象类,对外提供一个进行增删改查的接口
            executeQureyTemplate(String sql, Class clzz, Object[] params, CallBack back):查询模板方法,用户需传入
        查询语句,封装返回数据的类对象,参数列表,CallBack 对象 并实现doExecute()方法
        executeDML(String sql,Object[] params):提交执行sql语句，返回受影响行数
        inset(Object object):把一个对象数据更新到数据库中
        delete(Class clzz,Object id):删除传入类映射表 以id为主键
        delete(Object clzz):删除对象所对应记录,对象值对应到记录
        update(Object object,String[] fileName):更新对象对应记录,并且只更新值到对应记录
        queryRows(String sql,Class clazz,Object[] params):查询返回多行记录,并返回一个集合,并将每行记录封装到指定的Class对象
        queryUinqueRow(String sql,Class clazz,Object[] params):查询返回一行记录
        queryValue(String sql,Object[] parames):查询返回一个结果
        queryNumber(String sql,Object[] parames):查询返回一个数字
         queryPagenate(int pageNum,int size):抽象方法 分页函数


            

        

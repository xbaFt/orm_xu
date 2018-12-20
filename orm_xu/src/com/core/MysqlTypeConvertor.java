package com.core;

/**
 * mysql数据库数据类型转换
 */
public class MysqlTypeConvertor implements TypeConvertor {
    @Override
    public String databaseType2JavaType(String columnType) {
        if ("varchar".equalsIgnoreCase(columnType)||"char".equalsIgnoreCase(columnType)){
            return "String";
        }else if ("int".equalsIgnoreCase(columnType)
                ||"tinyint".equalsIgnoreCase(columnType)
                ||"smallint".equalsIgnoreCase(columnType)
                ||"integer".equalsIgnoreCase(columnType)){
                return "Integer";
            }else if ("bigint".equalsIgnoreCase(columnType)){
            return "Long";
        }else if ("double".equalsIgnoreCase(columnType)||"float".equalsIgnoreCase(columnType)){
            return "Double";
        }else if ("clon".equalsIgnoreCase(columnType)){
            return "java.sql.CLob";
        }else if ("blob".equalsIgnoreCase(columnType)){
            return "java.sql.Blob";
        }else if ("date".equalsIgnoreCase(columnType)){
            return "java.sql.Date";
        }else if ("time".equalsIgnoreCase(columnType)){
            return "java.sql.Time";
        }else if ("timestamp".equalsIgnoreCase(columnType)){
            return "java.sql.Timestamp";
        }
        return null;
    }

    @Override
    public String javadataType2dataType(String javaDataType) {
        return null;
    }
}

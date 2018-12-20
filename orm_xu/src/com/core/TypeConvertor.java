package com.core;

/**
 * 负责java类型和数据库数据类型的互相转换
 */
public interface TypeConvertor {
    /**
     * 将数据库中的数据类型转换成java数据类型
     * @param columnType 数据库中字段数据类型
     * @return java数据类型
     */
    String databaseType2JavaType(String columnType);

    /**
     * 把java数据类型转换为数据库数据类型
     * @param javaDataType java数据类型
     * @return 数据库数据类型
     */
    String javadataType2dataType(String javaDataType);
}

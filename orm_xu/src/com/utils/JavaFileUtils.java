package com.utils;

import com.bean.ColummbInfo;
import com.bean.JavaFielGetSet;
import com.bean.TableInfo;
import com.core.DBMananger;
import com.core.MysqlTypeConvertor;
import com.core.TableContext;
import com.core.TypeConvertor;

import javax.sound.midi.VoiceStatus;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 封装了生成Java文件(源代码)常用的操作
 */
public class JavaFileUtils {
    /**
     * 根据传进来的字段信息转换成java对象
     * @param colummbInfo 字段信息
     * @param typeConvertor 类型转换器
     * @return java属性和set/get方法
     */
    public static JavaFielGetSet createJavaFileUtiles(ColummbInfo colummbInfo, TypeConvertor typeConvertor){
        JavaFielGetSet javaFielGetSet = new JavaFielGetSet();
        //把数据库数据类型转换成java数据类型
        String javaFieldType = typeConvertor.databaseType2JavaType(colummbInfo.getDataType());
        javaFielGetSet.setFieldInfo("\tprivate "+javaFieldType+" "+colummbInfo.getName()+";\n");
        /**
         * 生成get方法
         * public String getUser(){
         * return user;}
         */
        StringBuilder sbget = new StringBuilder();
        sbget.append("\tpublic "+javaFieldType+" get"+StringUtils.firstChar2UpperCase(colummbInfo.getName()+"(){\n"));
        //由于属性名与字段名称保持一直 所以直接返回字段名就行
        sbget.append("\t\treturn "+colummbInfo.getName()+";\n");
        sbget.append("\t}\n");
        javaFielGetSet.setGetIfo(sbget.toString());

        /**
         * 生成set方法
         * public void setUser(String user){
         * this.user = user;
         * }
         */

        StringBuilder sbset = new StringBuilder();
        sbset.append("\tpublic void "+"set"+StringUtils.firstChar2UpperCase(colummbInfo.getName())+"("+javaFieldType+" "+colummbInfo.getName()+"){\n");
        sbset.append("\t\tthis."+colummbInfo.getName()+" = "+colummbInfo.getName()+";\n");
        sbset.append("\t}\n");
        javaFielGetSet.setSetInfo(sbset.toString());
        //返回源码
        return javaFielGetSet;
    }

    public static void main(String[] args){
       /* ColummbInfo ci = new ColummbInfo("username","varchar",0);
        JavaFielGetSet jfg = createJavaFileUtiles(ci,new MysqlTypeConvertor());
        System.out.println(jfg);
       Map<String,TableInfo> map = TableContext.getTables();
       TableInfo t = map.get("emp");
       createJavaSrc(t,new MysqlTypeConvertor());
       for (TableInfo tableInfo:map.values()){
            createJavaPoFile(tableInfo, new MysqlTypeConvertor());
        }
        */
    }

    /**
     * 根据表信息生成java源码
     * @param tableInfo 表信息
     * @param typeConvertor 类型转换器
     * @return java源码
     */
    public static String createJavaSrc(TableInfo tableInfo,TypeConvertor typeConvertor){
        StringBuilder sbSrc = new StringBuilder();
        //拿到所有字段信息
        Map<String,ColummbInfo> mapInfo = tableInfo.getColumms();
        List<JavaFielGetSet> javaFields = new ArrayList<>();
        for (ColummbInfo c:mapInfo.values()){
            javaFields.add(createJavaFileUtiles(c,typeConvertor));
        }

        //生成package语句
        sbSrc.append("package "+DBMananger.getCof().getPoPackage()+";\n\n");

        //生成import
        sbSrc.append("import java.sql.*;\n");
        sbSrc.append("import java.util.*;\n\n");

        //生成声明语句
        sbSrc.append("public class "+StringUtils.firstChar2UpperCase(tableInfo.getName())+" {\n\n");
        //生成属性列表
        for (JavaFielGetSet f : javaFields){
            sbSrc.append(f.getFieldInfo());
        }
        sbSrc.append("\n\n");
        //生成get方法列表
        for (JavaFielGetSet get:javaFields){
            sbSrc.append(get.getGetIfo());
        }
        sbSrc.append("\n\n");
        //生成set方法列表
        for (JavaFielGetSet set:javaFields){
            sbSrc.append(set.getSetInfo());
        }
        sbSrc.append("}\n");
        return sbSrc.toString();
    }

    /**
     * 根据java源码字符串 在po包下生成java文件
     * @param tableInfo
     * @param typeConvertor
     */
    public static void createJavaPoFile(TableInfo tableInfo,TypeConvertor typeConvertor){
        String src = createJavaSrc(tableInfo,typeConvertor);
        String packagePath = DBMananger.getCof().getPoPackage().replaceAll("\\.","\\\\");
        String srcPath = DBMananger.getCof().getSrcPath()+"\\";
        File file = new File(srcPath+packagePath);
        //判断文件夹是否存在
        if (!file.exists()){
            file.mkdirs();
        }
        BufferedWriter bfw = null;
        try {
            bfw = new BufferedWriter(new FileWriter(file.getAbsoluteFile()+"/"+StringUtils.firstChar2UpperCase(tableInfo.getName()+".java")));
            bfw.write(src);
            System.out.println("创建类:"+tableInfo.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                bfw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

package com.bean;

/**
 * 封装配置信息
 */
public class Configuration {
    private String dirver;
    private String url;
    private String user;
    private String pwd;
    private String usingDB;
    /**
     * 项目源码路径
     */
    private String srcPath;
    /**
     * 扫描生成java的包(持久化对象)
     */
    private String poPackage;

    /**
     * query对象
     */
    private String query;

    /**
     * 链接池最大链接数
     */
    private int poolMax;

    public int getPoolMax() {
        return poolMax;
    }

    public void setPoolMax(int poolMax) {
        this.poolMax = poolMax;
    }

    public int getPoolMin() {
        return poolMin;
    }

    public void setPoolMin(int poolMin) {
        this.poolMin = poolMin;
    }

    /**
     * 链接池最小连接数
     */
    private int poolMin;

    public Configuration() {
    }

    public Configuration(String dirver, String url, String user, String pwd, String usingDB, String srcPath, String poPackage,String query) {
        this.dirver = dirver;
        this.url = url;
        this.user = user;
        this.pwd = pwd;
        this.usingDB = usingDB;
        this.srcPath = srcPath;
        this.poPackage = poPackage;
        this.query = query;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getDirver() {
        return dirver;
    }

    public void setDirver(String dirver) {
        this.dirver = dirver;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getUsingDB() {
        return usingDB;
    }

    public void setUsingDB(String usingDB) {
        this.usingDB = usingDB;
    }

    public String getSrcPath() {
        return srcPath;
    }

    public void setSrcPath(String srcPath) {
        this.srcPath = srcPath;
    }

    public String getPoPackage() {
        return poPackage;
    }

    public void setPoPackage(String poPackage) {
        this.poPackage = poPackage;
    }
}
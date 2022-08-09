package com.thoth.iqnoon.utils;

import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.CaseInsensitiveContainerFactory;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.dialect.MysqlDialect;
import com.jfinal.plugin.c3p0.C3p0Plugin;
import org.apache.commons.lang3.StringUtils;


import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InitDataUtils
{
    /**
     * 使用mysql数据库
     * 使用者不需关注数据库配置的初始化过程，本demo是借助jfinal框架初始化的数据库配置，
     * 使用者完全可以用其他方式代替
     */
    public static void initMysqlDruid()
    {

        C3p0Plugin c3p0Plugin = new C3p0Plugin("jdbc:mysql://127.0.0.1:3306/iqnoon?characterEncoding=utf-8&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai",
                "root", "iqnoon", "com.mysql.cj.jdbc.Driver", 1, 1, 1, 20, 1);
        c3p0Plugin.setDriverClass("com.mysql.cj.jdbc.Driver");
        c3p0Plugin.start();

        ActiveRecordPlugin arp = new ActiveRecordPlugin(c3p0Plugin);
        arp.setDialect(new MysqlDialect());
        arp.setDevMode(true);
        arp.setShowSql(true);
        arp.setContainerFactory(new CaseInsensitiveContainerFactory());
        arp.start();
    }

    /**
     * 表结构初始化
     */
    public static List<String> executeBatchSql()
    {
        BufferedReader bufferedReader = null;
        Connection conn = null;
        List<String> sqlList = new ArrayList<>();
        try
        {
            conn = Db.use().getConfig().getConnection();
            File file = new File("D:\\code\\iqnoon\\iqnoon-service\\src\\main\\resources\\region.sql");
            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8"));
            String line = "";
            while((line = bufferedReader.readLine()) != null){
                if (StringUtils.isNotBlank(line))
                    sqlList.add(line);
            }
            Statement statement = null;
            try {
                statement = conn.createStatement();
                for (String sql : sqlList) {
                    if(StringUtils.isBlank(sql)){
                        continue;
                    }
                    statement.execute(sql);
                }
            } catch(SQLException e) {
                //数据库连接失败异常处理
                e.printStackTrace();
            } finally {
                if (null != statement) {
                    try {
                        statement.close();
                    } catch (SQLException e) {

                    }
                }
                if (null != conn) {
                    try {
                        conn.close();
                    } catch (SQLException e) {

                    }
                }
            }
        }
        catch (Exception ex)
        {
            throw new RuntimeException("initTableDesign error!",ex);
        }
        finally
        {
            Db.use().getConfig().close(conn);
        }
        return sqlList;
    }

    public static void main(String[] args) {
        initMysqlDruid();
        executeBatchSql();
    }




}
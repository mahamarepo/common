package com.mahama.common.utils;

import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
    public static Boolean testMSSQLConnection(String url, String username, String password) {
        return testDBConnection("com.microsoft.sqlserver.jdbc.SQLServerDriver", url, username, password);
    }

    public static Boolean testDBConnection(String driverClassName, String url, String username, String password) {
        try {
            //加载数据库驱动类
            Class.forName(driverClassName);
            System.out.println("数据库驱动加载成功");  //返回加载驱动成功信息
        } catch (ClassNotFoundException e) {
            return false;
        }
        try {
            DriverManager.getConnection(url, username, password);//通过访问数据库的URL获取数据库连接对象 ，这里后两个参数分别是数据库的用户名及密码
            System.out.println("数据库连接成功");  //返回连接成功信息
            return true;
        } catch (SQLException e) {
            System.out.println("数据库连接失败");
        }
        return false;
    }
}

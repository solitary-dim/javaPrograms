package com.omdes.javaPrograms.crawler;

import java.sql.*;

import static com.omdes.javaPrograms.crawler.Config.*;

/**
 * Created with IntelliJ IDEA.
 * User: Solitary.Wang
 * Date: 2017/7/3
 * Time: 19:02
 */
public final class MySQLHelper {
    private static volatile MySQLHelper mySQLHelper;

    private Connection connection = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;

    public Connection getConnection() {
        return this.connection;
    }

    public Statement getStatement() {
        return this.statement;
    }

    public PreparedStatement getPreparedStatement() {
        return this.preparedStatement;
    }

    public ResultSet getResultSet() {
        return this.resultSet;
    }

    private MySQLHelper() {}

    public static MySQLHelper getInstance() {
        if (mySQLHelper == null) {
            synchronized (MySQLHelper.class) {
                if (mySQLHelper == null)        {
                    mySQLHelper = new MySQLHelper();
                }
            }
        }
        return mySQLHelper;
    }

    //建立与MySQL的连接
    public void openConnection() {
        //连接MySql数据库
        if (null == this.connection) {
            try {
                //加载MySql的驱动类
                Class.forName(DRIVER_NAME);
            } catch (ClassNotFoundException e) {
                System.out.println("找不到驱动程序类 ，加载驱动失败！");
                e.printStackTrace();
            }
            try {
                this.connection = DriverManager.getConnection(SQL_URL, SQL_USER, SQL_PASSWORD);
            } catch (SQLException e) {
                System.out.println("数据库连接失败！");
                e.printStackTrace();
            }
        }
    }

    //关闭与MySQL的连接
    public void closeConnection() {
        if (this.resultSet != null) {
            try {
                this.resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (this.statement !=null) {
            try {
                this.statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (this.preparedStatement !=null) {
            try {
                this.preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (this.connection != null) {
            try {
                this.connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public long getIdStart() {
        long id = 0L;

        //判断数据库中是否已经存在了数据
        if (!isEmpty()) {
            this.openConnection();
            try {
                this.statement = this.connection.createStatement();
                this.resultSet = this.statement.executeQuery("SELECT MAX(ID) FROM T_URL;");
                if (this.resultSet.next()) {
                    id = this.resultSet.getLong(1);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return id;
    }

    private boolean isEmpty() {
        boolean result = true;
        this.openConnection();
        try {
            this.statement = this.connection.createStatement();
            this.resultSet = this.statement.executeQuery("SELECT COUNT(1) FROM T_URL;");
            if (this.resultSet.next() && this.resultSet.getInt(1) > 0) {
                result = false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

}

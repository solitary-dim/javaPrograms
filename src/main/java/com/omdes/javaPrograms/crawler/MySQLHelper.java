package com.omdes.javaPrograms.crawler;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    private static PropertiesConfig config;

    public static MySQLHelper getInstance() {
        config = PropertiesConfig.getInstance();

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
                Class.forName(config.getMysqlDriverName());
            } catch (ClassNotFoundException e) {
                System.out.println("找不到驱动程序类 ，加载驱动失败！");
                e.printStackTrace();
            }
            try {
                this.connection = DriverManager.getConnection(config.getMysqlUrl(),
                        config.getMysqlUsername(),
                        config.getMysqlPassword());
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

    /**
     * 判断当前表是否为空
     * @param tableName 表名
     * @return true-空；false-非空
     */
    private boolean isEmpty(String tableName) {
        boolean result = true;
        this.openConnection();
        try {
            this.statement = this.connection.createStatement();
            this.resultSet = this.statement.executeQuery("SELECT COUNT(1) FROM " + tableName + ";");
            if (this.resultSet.next() && this.resultSet.getInt(1) > 0) {
                result = false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 得到当前表中已有数据的最大ID
     * @param tableName 表名
     * @return long 表中已有最大ID
     */
    public long getIdStart(String tableName) {
        long id = 0L;

        //判断数据库中是否已经存在了数据
        if (!isEmpty(tableName)) {
            this.openConnection();
            try {
                this.statement = this.connection.createStatement();
                this.resultSet = this.statement.executeQuery("SELECT MAX(ID) FROM "+ tableName + ";");
                if (this.resultSet.next()) {
                    id = this.resultSet.getLong(1);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return id;
    }

    public List<URLEntity> getUnvisitedUrl(String tableName) {
        List<URLEntity> list = new ArrayList<>();
        //判断数据库中是否已经存在了数据
        if (!isEmpty(tableName)) {
            this.openConnection();
            try {
                this.statement = this.connection.createStatement();
                this.resultSet = this.statement.executeQuery("SELECT * FROM "+ tableName + " WHERE DELETED_FLAG = 0 AND IS_USED = 0 AND LEVEL > 0;");
                while (this.resultSet.next()) {
                    URLEntity entity = new URLEntity();
                    entity.setId(resultSet.getLong(1));
                    entity.setName(resultSet.getString(2));
                    entity.setStatus(resultSet.getInt(3));
                    entity.setDeletedFlag(resultSet.getInt(4));
                    entity.setCreatedTime(resultSet.getTimestamp(5));
                    entity.setCreatedUserId(resultSet.getLong(6));
                    entity.setUpdatedTime(resultSet.getTimestamp(7));
                    entity.setUpdatedUserId(resultSet.getLong(8));
                    entity.setLevel(resultSet.getLong(9));
                    entity.setUrl(resultSet.getString(10));
                    entity.setIsUsed(resultSet.getInt(11));
                    entity.setCount(resultSet.getInt(12));
                    entity.setContent(resultSet.getString(13));
                    entity.setNotes(resultSet.getString(14));
                    list.add(entity);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return list;
    }
}

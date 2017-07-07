package com.omdes.javaPrograms.crawler.helper;

import com.omdes.javaPrograms.crawler.config.PropertiesConfig;
import com.omdes.javaPrograms.crawler.entity.URLEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static com.omdes.javaPrograms.crawler.config.BaseConfig.*;

/**
 * Created with IntelliJ IDEA.
 * User: Solitary.Wang
 * Date: 2017/7/3
 * Time: 19:02
 */
public final class MySQLHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(MySQLHelper.class);
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

    /**
     * 得到当前表中还未访问过的url
     * @param tableName 表名
     * @param type 0-all; 1-url；2-src
     * @return
     */
    public List<URLEntity> getUnvisitedUrl(String tableName, int type) {
        List<URLEntity> list = new ArrayList<>();
        //判断数据库中是否已经存在了数据
        if (!isEmpty(tableName)) {
            this.openConnection();
            try {
                StringBuilder sql = new StringBuilder("SELECT ID,LEVEL,URL,IS_USED,COUNT FROM ").
                        append(tableName).
                        append(" WHERE DELETED_FLAG = 0").
                        append(" AND IS_USED = 0");
                //判断是否针对特定标签查询出未访问过的url，否则查询出所有
                switch (type) {
                    case TYPE_PAGE:
                        //仅查询未访问过的a标签的href
                        sql.append(" AND LEVEL > 0;");
                        break;
                    case TYPE_IMAGE:
                        //仅查询未访问过的img标签的src
                        sql.append(" AND LEVEL = 0;");
                        break;
                    default:
                        break;
                }
                this.statement = this.connection.createStatement();
                this.resultSet = this.statement.executeQuery(sql.toString());
                while (this.resultSet.next()) {
                    URLEntity entity = new URLEntity();
                    entity.setId(resultSet.getLong(1));
                    entity.setLevel(resultSet.getLong(2));
                    entity.setUrl(resultSet.getString(3));
                    entity.setIsUsed(resultSet.getInt(4));
                    entity.setCount(resultSet.getInt(5));
                    list.add(entity);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * 得到当前表中已经访问过的url
     * @param tableName 表名
     * @param type 0-all; 1-url；2-src
     * @return
     */
    public List<URLEntity> getVisitedUrl(String tableName, int type) {
        List<URLEntity> list = new ArrayList<>();
        //判断数据库中是否已经存在了数据
        if (!isEmpty(tableName)) {
            this.openConnection();
            try {
                StringBuilder sql = new StringBuilder("SELECT ID,LEVEL,URL,IS_USED,COUNT FROM ").
                        append(tableName).
                        append(" WHERE DELETED_FLAG = 0").
                        append(" AND IS_USED = 0");
                //判断是否针对特定标签查询出已经访问过的url，否则查询出所有
                switch (type) {
                    case TYPE_PAGE:
                        //仅查询未访问过的a标签的href
                        sql.append(" AND LEVEL > 0;");
                        break;
                    case TYPE_IMAGE:
                        //仅查询未访问过的img标签的src
                        sql.append(" AND LEVEL = 0;");
                        break;
                    default:
                        break;
                }
                this.statement = this.connection.createStatement();
                this.resultSet = this.statement.executeQuery(sql.toString());
                while (this.resultSet.next()) {
                    URLEntity entity = new URLEntity();
                    entity.setId(resultSet.getLong(1));
                    entity.setLevel(resultSet.getLong(2));
                    entity.setUrl(resultSet.getString(3));
                    entity.setIsUsed(resultSet.getInt(4));
                    entity.setCount(resultSet.getInt(5));
                    list.add(entity);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * 连接数据库，将数据存入数据库
     * @param entity
     */
    public void saveUrl(URLEntity entity) {
        Date date = new Date();
        StringBuilder sql = new StringBuilder();
        sql = sql.append("INSERT INTO T_URL (ID, NAME, STATUS, DELETED_FLAG, CREATED_TIME, ").
                append("CREATED_USER_ID, UPDATED_TIME, UPDATED_USER_ID, LEVEL, URL, IS_USED, ").
                append("COUNT, CONTENT, NOTES) VALUES (").append(entity.getId()).append(",").
                append("'").append(entity.getName()).append("',").
                append(entity.getStatus()).append(",").
                append(entity.getDeletedFlag()).append(",").
                append("'").append(new Timestamp(date.getTime())).append("',").
                append(0L).append(",").
                append("'").append(new Timestamp(date.getTime())).append("',").
                append(0L).append(",").
                append(entity.getLevel()).append(",").
                append("'").append(entity.getUrl()).append("',").
                append(entity.getIsUsed()).append(",").
                append(entity.getCount()).append(",").
                append("'").append(entity.getContent()).append("',").
                append("'").append(entity.getNotes()).append("')");

        this.openConnection();
        try {
            Connection connection = this.connection;
            Statement pstmt= connection.createStatement();
            int result = pstmt.executeUpdate(sql.toString());
            LOGGER.info("insert into MySQL result: " + result);

            if (null != pstmt) {
                pstmt.close();
            }
        } catch (SQLException e) {
            LOGGER.error("SQLException!", e);
        }
    }

    /**
     * 连接数据库，将数据批量存入数据库
     * @param list
     */
    public void saveUrlList(List<URLEntity> list) {
        String sql = "INSERT INTO T_URL (ID, NAME, STATUS, DELETED_FLAG, CREATED_TIME, " +
                "CREATED_USER_ID, UPDATED_TIME, UPDATED_USER_ID, LEVEL, URL, IS_USED, " +
                "COUNT, CONTENT, NOTES) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        this.openConnection();
        try {
            Connection connection = this.connection;
            PreparedStatement pstmt = connection.prepareStatement(sql);
            connection.setAutoCommit(false);
            for (URLEntity entity: list) {
                Date date = new Date();
                pstmt.setLong(1, entity.getId());
                pstmt.setString(2, entity.getName());
                pstmt.setInt(3, entity.getStatus());
                pstmt.setInt(4, entity.getDeletedFlag());
                pstmt.setTimestamp(5, new Timestamp(date.getTime()));
                //pstmt.setLong(6, entity.getCreatedUserId());
                pstmt.setLong(6, 0L);
                pstmt.setTimestamp(7, new Timestamp(date.getTime()));
                //pstmt.setLong(8, entity.getUpdatedUserId());
                pstmt.setLong(8, 0L);
                pstmt.setLong(9, entity.getLevel());
                pstmt.setString(10, entity.getUrl());
                pstmt.setInt(11, entity.getIsUsed());
                pstmt.setInt(12, entity.getCount());
                pstmt.setString(13, entity.getContent());
                pstmt.setString(14, entity.getNotes());
                pstmt.addBatch();
            }
            pstmt.executeBatch();
            connection.commit();

            if (null != pstmt) {
                pstmt.close();
            }
        } catch (SQLException e) {
            LOGGER.error("SQLException!", e);
        }
    }

    /**
     * 连接数据库，将数据存入数据库
     * @param entity
     */
    public void updateUrl(URLEntity entity) {
        Date date = new Date();
        StringBuilder sql = new StringBuilder();
        sql = sql.append("UPDATE T_URL SET ").
                append("UPDATED_TIME = '").append(new Timestamp(date.getTime())).append("',").
                append("UPDATED_USER_ID = ").append(0L).append(",").
                append("LEVEL = ").append(entity.getLevel()).append(",").
                append("IS_USED = ,").append(entity.getIsUsed()).append(",").
                append("COUNT = ").append(entity.getCount()).append(",").
                append("CONTENT = '").append(entity.getContent()).append("',").
                append("NOTES = '").append(entity.getNotes()).append("'").
                append("WHERE ID = ").append(entity.getId());

        this.openConnection();
        try {
            Connection connection = this.connection;
            Statement pstmt= connection.createStatement();
            int result = pstmt.executeUpdate(sql.toString());
            LOGGER.info("update data result: " + result);

            if (null != pstmt) {
                pstmt.close();
            }
        } catch (SQLException e) {
            LOGGER.error("SQLException!", e);
        }
    }

    /**
     * 连接数据库，将数据批量存入数据库
     * @param list
     */
    public void updateUrlList(List<URLEntity> list) {
        String sql = "UPDATE T_URL SET UPDATED_TIME = ?, UPDATED_USER_ID = ?, LEVEL = ?," +
                " IS_USED = ?, COUNT = ?, CONTENT =?, NOTES = ? WHERE ID = ?";

        this.openConnection();
        try {
            Connection connection = this.connection;
            PreparedStatement pstmt = connection.prepareStatement(sql);
            connection.setAutoCommit(false);
            for (URLEntity entity: list) {
                Date date = new Date();
                pstmt.setTimestamp(1, new Timestamp(date.getTime()));
                pstmt.setLong(2, 0L);
                pstmt.setLong(3, entity.getLevel());
                pstmt.setInt(4, entity.getIsUsed());
                pstmt.setInt(5, entity.getCount());
                pstmt.setString(6, entity.getContent());
                pstmt.setString(7, entity.getNotes());
                pstmt.setLong(8, entity.getId());
                pstmt.addBatch();
            }
            pstmt.executeBatch();
            connection.commit();

            if (null != pstmt) {
                pstmt.close();
            }
        } catch (SQLException e) {
            LOGGER.error("SQLException!", e);
        }
    }

    /**
     * 连接数据库，将黑名单批量存入数据库
     * @param blackList
     */
    public void saveBlackList(Set<String> blackList) {
        long id = mySQLHelper.getIdStart("T_BLACKLIST");

        String sql = "INSERT INTO T_BLACKLIST (ID, NAME, COUNT, NOTES) VALUES (?,?,?,?)";

        try {
            Connection connection = this.connection;
            PreparedStatement pstmt = connection.prepareStatement(sql);
            connection.setAutoCommit(false);
            for (String str: blackList) {
                pstmt.setLong(1, id);
                pstmt.setString(2, str);
                pstmt.setInt(3, 1);
                pstmt.setString(4, "");
                pstmt.addBatch();
            }
            pstmt.executeBatch();
            connection.commit();

            if (null != pstmt) {
                pstmt.close();
            }
        } catch (SQLException e) {
            LOGGER.error("SQLException!", e);
        }
    }
}

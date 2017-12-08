package org.cly.helper;

import org.cly.util.PropsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * 数据库操作助手类
 *
 * @author chen
 * @date 2017-12-05 16:39
 */
public class DatabaseHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseHelper.class);
    private static final ThreadLocal<Connection> CONNECTION_HOLDER = new ThreadLocal<Connection>();

    private static final String DRIVER;
    private static final String URL;
    private static final String USERNAME;
    private static final String PASSWORD;

    static {
        Properties conf = PropsUtil.loadProps("smart.properties");
        DRIVER = conf.getProperty("jdbc.driver");
        URL = conf.getProperty("jdbc.url");
        USERNAME = conf.getProperty("jdbc.username");
        PASSWORD = conf.getProperty("jdbc.password");
        try {
            Class.forName(DRIVER);
        }catch (ClassNotFoundException e){
            LOGGER.error("can not load jdbc driver", e);
        }
    }

    /**
     * 获取数据库连接
     */
    public static Connection getConnection(){
        Connection conn = CONNECTION_HOLDER.get();
        if(conn == null){
            try {
                conn = DriverManager.getConnection(URL ,USERNAME, PASSWORD);
            }catch (SQLException e){
                LOGGER.error("get connection failure",e);
                throw  new RuntimeException();
            }finally {
                CONNECTION_HOLDER.set(conn);
            }
        }
        return conn;
    }

    /**
     * 关闭数据库连接
     */
    public static void closeConnection(Connection conn){
        if(conn != null){
            try {
                conn.close();
            }catch (SQLException e){
                LOGGER.error("close connection failure", e);
            }
        }
    }

    /**
     * 开启事物
     */
    public static void beginTransaction(){
        Connection conn = getConnection();
        if(conn != null){
            try {
                conn.setAutoCommit(false);
            }catch (SQLException e){
                LOGGER.error("begin transaction failure", e);
                throw  new RuntimeException(e);
            }finally {
                CONNECTION_HOLDER.set(conn);
            }
        }
    }

    /**
     * 提交事物
     */
    public static void commitTransaction(){
        Connection conn = getConnection();
        if(conn != null){
            try {
                conn.commit();
                conn.close();
            }catch (SQLException e){
                LOGGER.error("commit transaction failure", e);
                throw new RuntimeException(e);
            } finally {
                CONNECTION_HOLDER.remove();
            }
        }
    }

    /**
     * 回滚事物
     */
    public static void rollbackTransaction(){
        Connection conn = getConnection();
        if(conn !=null){
            try {
                conn.rollback();
                conn.close();
            } catch (SQLException e){
              LOGGER.error("rollback transaction failure", e);
              throw new RuntimeException(e);
            }finally {
                CONNECTION_HOLDER.remove();
            }
        }
    }
}

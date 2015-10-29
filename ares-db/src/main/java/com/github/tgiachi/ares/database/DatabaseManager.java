package com.github.tgiachi.ares.database;

import com.github.tgiachi.ares.annotations.AresDatabaseManager;
import com.github.tgiachi.ares.interfaces.database.IDatabaseManager;
import com.github.tgiachi.ares.sessions.SessionManager;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Manager del database e connection pool
 */
@AresDatabaseManager
public class DatabaseManager implements IDatabaseManager {

    private static Logger logger = Logger.getLogger(DatabaseManager.class);

    private ComboPooledDataSource mDatasource;


    @Override
    public void start() {
        initConnection();

    }

    @Override
    public void dispose() {

    }

    private void initConnection()
    {
        try
        {
            mDatasource = new ComboPooledDataSource();
            mDatasource.setJdbcUrl(SessionManager.getConfig().getDatabaseConfig().getUrl());
            mDatasource.setUser(SessionManager.getConfig().getDatabaseConfig().getUsername());
            mDatasource.setPassword(SessionManager.getConfig().getDatabaseConfig().getPassword());
            mDatasource.setDriverClass(SessionManager.getConfig().getDatabaseConfig().getDriverClass());


            mDatasource.setInitialPoolSize(10);
            mDatasource.setMinPoolSize(10);
            mDatasource.setMaxPoolSize(100);
            mDatasource.setMaxStatements(200);

            try
            {
                Connection testConnection = mDatasource.getConnection();

                Statement testStatement = testConnection.createStatement();
                testStatement.execute("select * from information_schema.tables;");

                testStatement.close();
                testConnection.close();

                log(Level.INFO, "Database layer is ready");

            }
            catch (SQLException ex)
            {
                log(Level.FATAL, "Error during connect to database: %s", ex.getErrorCode());
            }


        }
        catch (Exception ex)
        {
            log(Level.ERROR, "Error during connecting to database => %s", ex.getMessage());
        }
    }

    protected void log(Level level, String text, Object ... args)
    {
        logger.log(level, String.format(text, args));
    }
}

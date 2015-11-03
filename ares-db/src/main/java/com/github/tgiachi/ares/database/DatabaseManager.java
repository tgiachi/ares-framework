package com.github.tgiachi.ares.database;

import com.github.tgiachi.ares.annotations.AresDatabaseManager;
import com.github.tgiachi.ares.annotations.database.AresEntity;
import com.github.tgiachi.ares.annotations.database.AresEntityField;
import com.github.tgiachi.ares.data.db.AresQuery;
import com.github.tgiachi.ares.interfaces.database.IAresQuery;
import com.github.tgiachi.ares.interfaces.database.IDatabaseManager;
import com.github.tgiachi.ares.sessions.SessionManager;
import com.github.tgiachi.ares.utils.ReflectionUtils;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Manager del database e connection pool
 */
@AresDatabaseManager
public class DatabaseManager implements IDatabaseManager {

    private static Logger logger = Logger.getLogger(DatabaseManager.class);

    private ComboPooledDataSource mDatasource;

    private HashMap<Class<?>, HashMap<String, Field>> mMappedEntities = new HashMap<>();




    @Override
    public void start() {

        mapEntities();
        initConnection();


    }

    private void mapEntities() {

        Set<Class<?>> classes = ReflectionUtils.getAnnotation(AresEntity.class);

        try
        {
            log(Level.INFO, "Found %s entities to map", classes.size());

            for(Class<?> classz : classes)
            {
                HashMap<String, Field> mMappedFields = new HashMap<>();

                AresEntity tableAnnotation = classz.getAnnotation(AresEntity.class);


                        log(Level.INFO, "Start to map class %s", classz.getSimpleName());

                for (Field f : classz.getDeclaredFields())
                {
                    if (f.isAnnotationPresent(AresEntityField.class))
                    {
                        AresEntityField annField = f.getAnnotation(AresEntityField.class);
                        log(Level.INFO, "%s.%s -> %s", classz.getSimpleName(), f.getName(), annField.value());

                        mMappedFields.put(annField.value(), f);

                    }
                }

                mMappedEntities.put(classz, mMappedFields);

            }

        }
        catch (Exception ex)
        {
            log(Level.FATAL, "Error during scan entities => %s", ex.getMessage());
        }
    }

    @Override
    public void shutdown() {
        mDatasource.close();
    }

    @Override
    public <T> T mapEntity(IAresQuery query, Class<?> classz) {

        List<T> results = new ArrayList<>();

        try
        {

            HashMap<String, Field> fields = mMappedEntities.get(classz);

            while ( query.getResultSet().next())
            {
                T entity = (T)classz.newInstance();

                for (String key : fields.keySet())
                {
                    String value = query.getResultSet().getString(key);

                    fields.get(key).setAccessible(true);
                    fields.get(key).set(entity, value);
                }

                results.add(entity);

            }

            if (results.size() == 1)
                 return results.get(0);
            else
                return (T)results;

        }
        catch (Exception ex)
        {
            log(Level.INFO, "Error during map query to entitiy -> %s", ex.getMessage());
        }


        return null;
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

            if (!SessionManager.getConfig().getDatabaseConfig().getPassword().equals(""))
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

    @Override
    public AresQuery getNewQuery() {
        AresQuery aresQuery = new AresQuery();
        try {
            aresQuery.setConnection(mDatasource.getConnection());
            aresQuery.setQuery(aresQuery.getConnection().createStatement());

        }
        catch (Exception ex)
        {
            log(Level.FATAL, "Error during get new query => %s", ex.getMessage());
        }

        return aresQuery;
    }

    @Override
    public void disposeQuery(AresQuery query) {

        try {
            query.getQuery().closeOnCompletion();
            query.getConnection().close();
        }
        catch (Exception ex)
        {
            log(Level.FATAL, "Error during close query => %s", ex.getMessage());
        }
    }
}

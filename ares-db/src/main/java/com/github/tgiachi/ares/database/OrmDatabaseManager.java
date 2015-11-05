package com.github.tgiachi.ares.database;

import com.github.tgiachi.ares.annotations.AresDatabaseManager;
import com.github.tgiachi.ares.data.db.AresQuery;

import com.github.tgiachi.ares.interfaces.database.IAresQuery;
import com.github.tgiachi.ares.interfaces.database.IDatabaseManager;
import com.github.tgiachi.ares.interfaces.database.IOrmManager;
import com.github.tgiachi.ares.sessions.SessionManager;
import com.github.tgiachi.ares.utils.ReflectionUtils;
import org.apache.log4j.Logger;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import javax.persistence.Entity;

import java.util.*;

/**
 * Componente per
 */
@AresDatabaseManager
public class OrmDatabaseManager implements IDatabaseManager, IOrmManager {

    private Logger logger = Logger.getLogger(OrmDatabaseManager.class);

    private SessionFactory mSessionFactory;

    @Override
    public AresQuery getNewQuery() {
        return null;
    }

    @Override
    public void disposeQuery(AresQuery query) {

    }

    @Override
    public void shutdown() {

    }

    @Override
    public <T> T mapEntity(IAresQuery query, Class<?> entityClass) {
        return null;
    }

    @Override
    public void start() {
       try
       {
           Set<Class<?>> classes = ReflectionUtils.getAnnotation(Entity.class);
           ArrayList<Class<?>> arrayList = new ArrayList<>();
           arrayList.addAll(classes);


           Properties properties = new Properties();


           properties.put("hibernate.connection.url", SessionManager.getDatabaseConfig().getUrl());
           properties.put("hibernate.connection.driver_class", SessionManager.getDatabaseConfig().getDriverClass());
           properties.put("hibernate.connection.username", SessionManager.getDatabaseConfig().getUsername());
           properties.put("hibernate.connection.password", SessionManager.getDatabaseConfig().getPassword());
           properties.put("hibernate.archive.autodetection", "class, hbm");

           properties.put("hibernate.show_sql", "false");
           properties.put("hibernate.format_sql", "false");
           properties.put("hibernate.hbm2ddl.auto", "create");

           properties.put("hibernate.connection.provider_class", "org.hibernate.connection.C3P0ConnectionProvider");

           properties.put("hibernate.c3p0.max_size", "100");
           properties.put("hibernate.c3p0.min_size", "10");
           properties.put("hibernate.c3p0.acquire_increment", "1");
           properties.put("hibernate.c3p0.idle_test_period", "300");
           properties.put("hibernate.c3p0.max_statements", "0");
           properties.put("hibernate.c3p0.timeout", "100");
           properties.put("hibernate.current_session_context_class","org.hibernate.context.internal.ThreadLocalSessionContext");

           Configuration configuration = new Configuration().addProperties(properties);
            for (Class<?> classz : classes)
            {
                configuration.addAnnotatedClass(classz);
                logger.info(String.format("Mapping entity %s", classz.getSimpleName()));
            }

           mSessionFactory = configuration.configure().buildSessionFactory();



       }
       catch (Exception ex)
       {
           logger.fatal(String.format("Error during start entityManager => %s", ex.getMessage()));
           ex.printStackTrace();
       }
    }

    @Override
    public void dispose() {

    }

    @Override
    public void persist(Object object) {


        try
        {
            Transaction t = mSessionFactory.getCurrentSession().beginTransaction();
            mSessionFactory.getCurrentSession().persist(object);
            t.commit();

        }
        catch (Exception ex)
        {
            logger.fatal(String.format("Error during persist entity %s => %s ", object.getClass(), ex.getMessage()));
        }

    }

    @Override
    public List<?> executeQuery(String hsql) {

        List<?> results = null;
        Session session = null;
        try
        {
            session = mSessionFactory.openSession();
            results = session.createQuery(hsql).list();
        }
        catch (Exception ex)
        {
            logger.fatal(String.format("Error during execute query %s => %s", hsql, ex.getMessage()));
        }
        finally
        {
            if (session != null && session.isOpen())
            {
                session.close();
                session = null;
            }
        }
        return results;
    }

    @Override
    public Object createQuery(String hsql)
    {
        return mSessionFactory.getCurrentSession().createQuery(hsql);
    }
}

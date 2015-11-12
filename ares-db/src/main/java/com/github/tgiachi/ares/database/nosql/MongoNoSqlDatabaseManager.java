package com.github.tgiachi.ares.database.nosql;

import com.github.tgiachi.ares.annotations.database.nosql.NoSqlEntity;
import com.github.tgiachi.ares.interfaces.database.INoSqlDatabaseManager;
import com.github.tgiachi.ares.utils.ReflectionUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import java.util.*;

/**
 * NoSQL database per gestire mongoDB
 */
public class MongoNoSqlDatabaseManager implements INoSqlDatabaseManager {

    private static Logger logger = Logger.getLogger(MongoNoSqlDatabaseManager.class);

    final Morphia morphia = new Morphia();

    private List<Datastore> datastores;


    private HashMap<String, Class<?>> classes = new HashMap<>();


    @Override
    public void init() {
        try
        {

            scanClasses();
        }
        catch (Exception ex)
        {
            log(Level.FATAL, "Error during initializing Mongo NoSQL driver => %s", ex.getMessage());
        }
    }

    private void scanClasses()
    {
        log(Level.INFO, "Scanning classes of NoSQL ");

        Set<Class<?>> classes = ReflectionUtils.getAnnotation(NoSqlEntity.class);

        log(Level.INFO, "Found %s NoSQL classes", classes.size());

        for (Class<?> classz : classes)
        {
            NoSqlEntity annotation = classz.getAnnotation(NoSqlEntity.class);


        }


    }

    private void addDatastore(Datastore datastore)
    {
        if (datastores == null)
            datastores = new ArrayList<>();

        ArrayList<Datastore> _datastores = new ArrayList<>();

        _datastores.addAll(datastores);
        _datastores.add(datastore);

        datastores = Collections.unmodifiableList(_datastores);
    }

    protected void log(Level level, String text, Object ... args)
    {
        logger.log(level, String.format(text, args));
    }
}

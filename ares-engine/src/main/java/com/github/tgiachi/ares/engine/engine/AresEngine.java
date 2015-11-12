package com.github.tgiachi.ares.engine.engine;

import com.github.tgiachi.ares.annotations.AresDatabaseManager;
import com.github.tgiachi.ares.annotations.AresFileSystemManager;
import com.github.tgiachi.ares.data.debug.GenerationStat;
import com.github.tgiachi.ares.engine.config.ConfigManager;
import com.github.tgiachi.ares.engine.container.AresContainer;
import com.github.tgiachi.ares.engine.dispatcher.DefaultDispatcher;
import com.github.tgiachi.ares.interfaces.container.IAresContainer;
import com.github.tgiachi.ares.interfaces.database.IDatabaseManager;
import com.github.tgiachi.ares.interfaces.database.INoSqlDatabaseManager;
import com.github.tgiachi.ares.interfaces.dispacher.IAresDispatcher;
import com.github.tgiachi.ares.interfaces.engine.IAresEngine;
import com.github.tgiachi.ares.interfaces.fs.IFileSystemManager;
import com.github.tgiachi.ares.sessions.SessionManager;
import com.github.tgiachi.ares.utils.ReflectionUtils;
import com.google.common.base.Strings;
import lombok.Getter;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.util.Set;

/**
 * Motore del framework
 */
public class AresEngine implements IAresEngine {

    private static Logger logger = Logger.getLogger(AresEngine.class);

    @Getter
    private IDatabaseManager databaseManager;

    @Getter
    private IFileSystemManager fileSystemManager;

    @Getter
    private IAresDispatcher dispatcher;

    @Getter
    private IAresContainer container;

    @Getter
    private INoSqlDatabaseManager noSqlDatabaseManager;



    @Override
    public void start() {


        if (new ConfigManager().initialize())
        {

            subscribeQueueEvents();

            initComponents();
            initContainer();

            loadDefaultDispatcher();
        }
        else
        {
            log(Level.FATAL, "Cannot start Ares application! Check log!");
        }
    }

    private void subscribeQueueEvents() {

        SessionManager.subscribe("DEBUG_GENERATION", o -> SessionManager.getGenerationStats().add((GenerationStat) o));
    }

    @Override
    public void shutdown() {

        getFileSystemManager().shutdown();
        getDatabaseManager().shutdown();
    }

    private void initContainer()
    {
        container = new AresContainer();
        container.init(this);
    }

    private void loadDefaultDispatcher() {

        dispatcher = new DefaultDispatcher(this);
    }


    private void initComponents()
    {
        try
        {
            loadFilesystemManager();

            loadDatabaseManager();

            loadNoSqlDatabaseManager();
        }
        catch (Exception ex)
        {
            log(Level.ERROR, "Error during load components => %s ", ex.getMessage());
        }

    }

    private void loadNoSqlDatabaseManager()
    {
        try
        {
            if (!Strings.isNullOrEmpty(SessionManager.getConfig().getNoSqlDatabaseManager()))
            {

            }
            else
            {
                log(Level.WARN, "NoSQL database manager is empty!");
            }

        }
        catch (Exception ex)
        {
            log(Level.FATAL, "Error during load NoSql Database manager => %s", ex.getMessage());
        }

    }

    private void loadDatabaseManager()
    {
        try
        {
            if (!Strings.isNullOrEmpty(SessionManager.getConfig().getDatabaseManager())) {
                Set<Class<?>> classes = ReflectionUtils.getAnnotation(AresDatabaseManager.class);
                Class<?> dbManClass = classes.stream().filter(s -> s.getName().equals(SessionManager.getConfig().getDatabaseManager())).findFirst().get();

                databaseManager = (IDatabaseManager) dbManClass.newInstance();
                databaseManager.start();

                log(Level.INFO, "Database manager is : %s", databaseManager.getClass().getName());
            }
            else
            {
                log(Level.WARN, "SQL database manager is empty!");
            }
        }
        catch (Exception ex)
        {
            log(Level.FATAL, "Error during load class DatabaseManager => %s", ex.getMessage());
        }
    }

    private void loadFilesystemManager()
    {
        try
        {
            Set<Class<?>> classes = ReflectionUtils.getAnnotation(AresFileSystemManager.class);

            Class<?> fsManClass = classes.stream().filter(s -> s.getName().equals(SessionManager.getConfig().getFilesystemManager())).findFirst().get();

            fileSystemManager = (IFileSystemManager)fsManClass.newInstance();
            fileSystemManager.start();

            log(Level.INFO, "Filesystem manager is : %s", fileSystemManager.getClass().getName());

        }
        catch (Exception ex)
        {
            log(Level.FATAL, "Error during load class FilesystemManager => %s", ex.getMessage());
        }
    }

    protected void log(Level level, String text, Object ... args)
    {
        logger.log(level, String.format(text, args));
    }
}

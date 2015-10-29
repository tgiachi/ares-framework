package com.github.tgiachi.ares.engine.engine;

import com.github.tgiachi.ares.annotations.AresDatabaseManager;
import com.github.tgiachi.ares.annotations.AresFileSystemManager;
import com.github.tgiachi.ares.data.config.AresConfig;
import com.github.tgiachi.ares.engine.reflections.ReflectionUtils;
import com.github.tgiachi.ares.engine.serializer.JsonSerializer;
import com.github.tgiachi.ares.engine.utils.EngineConst;
import com.github.tgiachi.ares.interfaces.database.IDatabaseManager;
import com.github.tgiachi.ares.interfaces.engine.IAresEngine;
import com.github.tgiachi.ares.interfaces.fs.IFileSystemManager;
import com.github.tgiachi.ares.sessions.SessionManager;
import lombok.Getter;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.List;
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

    private String configFilename;


    @Override
    public void start() {

        loadConfig();
        initComponents();
    }

    private void loadConfig()
    {
        try
        {


            SessionManager.setRootDirectory(EngineConst.getAresBasedirectory());
            SessionManager.setAppDirectory(EngineConst.getAresBasedirectory() + File.separator + EngineConst.getAresAppName());
            SessionManager.setAppConfigDirectory(SessionManager.getAppDirectory() + File.separator + EngineConst.DEFAULT_CONFIG_DIRECTORY + File.separator);

            configFilename = SessionManager.getAppConfigDirectory() + "aresframework.conf";


            log(Level.DEBUG, "Current config file is %s", configFilename);

            if (!new File(SessionManager.getRootDirectory()).exists())
            {
                log(Level.WARN, "Creating base directory: %s", EngineConst.getAresBasedirectory());
                new File(SessionManager.getRootDirectory()).mkdirs();
            }

            if (!new File(SessionManager.getAppDirectory()).exists())
            {
                log(Level.WARN, "Creating app directory: %s", SessionManager.getAppDirectory());
                new File(SessionManager.getAppDirectory()).mkdirs();
            }


            if (!new File(SessionManager.getAppConfigDirectory()).exists())
            {
                log(Level.WARN, "Creating app config directory: %s", SessionManager.getAppConfigDirectory());
                new File(SessionManager.getAppConfigDirectory()).mkdirs();
            }



            if (!new File(configFilename).exists())
            {
                SessionManager.setConfig( new AresConfig());
                SessionManager.getConfig().getHeader().setAuthor("Ares webframework");
                SessionManager.getConfig().getHeader().setDescription("Default config");
                SessionManager.getConfig().addEntry("test_value", "test");

                String json = JsonSerializer.Serialize(SessionManager.getConfig());

                FileUtils.writeStringToFile(new File(configFilename), json);


            }

            log(Level.INFO, "Loading config file: %s", configFilename);

            SessionManager.setConfig(JsonSerializer.Deserialize(FileUtils.readFileToString(new File(configFilename)), AresConfig.class));

            log(Level.INFO, "Loading config file is OK");

        }
        catch (Exception ex)
        {

        }

    }

    private void initComponents()
    {
        try
        {
            loadFilesystemManager();

            loadDatabaseManager();
        }
        catch (Exception ex)
        {
            log(Level.ERROR, "Errore durante l'inizializzazione dei componenti => %s ", ex.getMessage());
        }

    }

    private void loadDatabaseManager()
    {
        try
        {
            Set<Class<?>> classes = ReflectionUtils.getAnnotation(AresDatabaseManager.class);
            Class<?> dbManClass = classes.stream().filter(s -> s.getName().equals(SessionManager.getConfig().getDatabaseManager())).findFirst().get();

            databaseManager = (IDatabaseManager)dbManClass.newInstance();
            databaseManager.start();

            log(Level.INFO, "Database manager is : %s", databaseManager.getClass().getName());

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

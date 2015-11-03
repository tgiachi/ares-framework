package com.github.tgiachi.ares.engine.engine;

import com.github.tgiachi.ares.annotations.AresDatabaseManager;
import com.github.tgiachi.ares.annotations.AresFileSystemManager;
import com.github.tgiachi.ares.data.config.AresConfig;
import com.github.tgiachi.ares.data.config.AresRouteEntry;
import com.github.tgiachi.ares.data.debug.GenerationStat;
import com.github.tgiachi.ares.engine.container.AresContainer;
import com.github.tgiachi.ares.engine.dispatcher.DefaultDispatcher;
import com.github.tgiachi.ares.utils.ReflectionUtils;
import com.github.tgiachi.ares.engine.serializer.JsonSerializer;
import com.github.tgiachi.ares.engine.utils.EngineConst;
import com.github.tgiachi.ares.interfaces.container.IAresContainer;
import com.github.tgiachi.ares.interfaces.database.IDatabaseManager;
import com.github.tgiachi.ares.interfaces.dispacher.IAresDispatcher;
import com.github.tgiachi.ares.interfaces.engine.IAresEngine;
import com.github.tgiachi.ares.interfaces.fs.IFileSystemManager;
import com.github.tgiachi.ares.sessions.SessionManager;
import lombok.Getter;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import rx.functions.Action1;

import java.io.File;
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

    private String configFilename;


    @Override
    public void start() {

        loadConfig();

        subscribeQueueEvents();

        initComponents();
        initContainer();



        loadDefaultDispacher();
    }

    private void subscribeQueueEvents() {

        SessionManager.subscribe("DEBUG_GENERATION", o -> SessionManager.getGenerationStats().add((GenerationStat) o));
    }

    @Override
    public void shutdown() {
        getDatabaseManager().shutdown();
    }

    private void initContainer()
    {
        container = new AresContainer();
        container.init(this);
    }

    private void loadDefaultDispacher() {

        dispatcher = new DefaultDispatcher(this);
    }

    private void loadConfig()
    {
        try
        {

            SessionManager.getDirectoriesConfig().setRootDirectory(EngineConst.getAresBasedirectory());
            SessionManager.getDirectoriesConfig().setAppDirectory(EngineConst.getAresBasedirectory() + File.separator + EngineConst.getAresAppName());
            SessionManager.getDirectoriesConfig().setAppConfigDirectory(SessionManager.getDirectoriesConfig().getAppDirectory() + File.separator + EngineConst.DEFAULT_CONFIG_DIRECTORY + File.separator);
            SessionManager.getDirectoriesConfig().setTemplateDirectory(SessionManager.getDirectoriesConfig().getAppDirectory() + File.separator + EngineConst.DEFAULT_TEMPLATE_DIRECTORY);



            configFilename = SessionManager.getDirectoriesConfig().getAppConfigDirectory() + "aresframework.conf";


            log(Level.DEBUG, "Current config file is %s", configFilename);

            if (!new File(SessionManager.getDirectoriesConfig().getRootDirectory()).exists())
            {
                log(Level.WARN, "Creating base directory: %s", EngineConst.getAresBasedirectory());
                new File(SessionManager.getDirectoriesConfig().getRootDirectory()).mkdirs();
            }

            if (!new File(SessionManager.getDirectoriesConfig().getAppDirectory()).exists())
            {
                log(Level.WARN, "Creating app directory: %s", SessionManager.getDirectoriesConfig().getAppDirectory());
                new File(SessionManager.getDirectoriesConfig().getAppDirectory()).mkdirs();
            }


            if (!new File(SessionManager.getDirectoriesConfig().getAppConfigDirectory()).exists())
            {
                log(Level.WARN, "Creating app config directory: %s", SessionManager.getDirectoriesConfig().getAppConfigDirectory());
                new File(SessionManager.getDirectoriesConfig().getAppConfigDirectory()).mkdirs();
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

            try
            {
                if (SessionManager.getConfig().getRoutes().getStaticRoutes().isEmpty())
                {
                    AresRouteEntry entry = new AresRouteEntry();
                    entry.setUrlMap("/imgs/*");
                    entry.setDirectory("imgs/");
                    entry.setProcessorClass("");

                    SessionManager.getConfig().getRoutes().getStaticRoutes().add(entry);

                }
                FileUtils.writeStringToFile(new File(configFilename), JsonSerializer.Serialize(SessionManager.getConfig()));
            }
            catch (Exception ex)
            {
                log(Level.FATAL, "Error during saving config => %s", ex.getMessage());
            }

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

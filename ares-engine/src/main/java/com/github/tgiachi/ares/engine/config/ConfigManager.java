package com.github.tgiachi.ares.engine.config;

import com.github.tgiachi.ares.annotations.actions.RequestType;
import com.github.tgiachi.ares.data.config.*;
import com.github.tgiachi.ares.engine.processors.LessResourceProcessor;
import com.github.tgiachi.ares.engine.serializer.JsonSerializer;
import com.github.tgiachi.ares.engine.utils.EngineConst;
import com.github.tgiachi.ares.sessions.SessionManager;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;

/**
 * Gestore della configurazione
 */
public class ConfigManager {

    private Logger logger = Logger.getLogger(ConfigManager.class);


    public ConfigManager()
    {
        log(Level.INFO, "Initializing configuration");

    }


    public boolean initialize()
    {
        try
        {

            SessionManager.getDirectoriesConfig().setRootDirectory(EngineConst.getAresBasedirectory());
            SessionManager.getDirectoriesConfig().setAppDirectory(EngineConst.getAresBasedirectory() + File.separator + EngineConst.getAresAppName());

            String appDirectory = SessionManager.getDirectoriesConfig().getAppDirectory();

            SessionManager.getDirectoriesConfig().setAppConfigDirectory(appDirectory + File.separator + EngineConst.DEFAULT_CONFIG_DIRECTORY + File.separator);
            SessionManager.getDirectoriesConfig().setTemplateDirectory(appDirectory + File.separator + EngineConst.DEFAULT_TEMPLATE_DIRECTORY);
            SessionManager.getDirectoriesConfig().setAppDatabaseDirectory(appDirectory + File.separator + EngineConst.DEFAULT_DB_CONFIG_DIRECTORY + File.separator);
            SessionManager.getDirectoriesConfig().setAppRoutesDirectory(appDirectory + File.separator + EngineConst.DEFAULT_ROUTES_CONFIG_DIRECTORY + File.separator);
            SessionManager.getDirectoriesConfig().setViewsDirectory(appDirectory + File.separator + EngineConst.DEFAULT_VIEWS_DIRECTORY + File.separator);

            log(Level.INFO, "- Ares application directories:");
            log(Level.INFO, "* Root directory: %s", SessionManager.getDirectoriesConfig().getRootDirectory());
            log(Level.INFO, "* App directory: %s", SessionManager.getDirectoriesConfig().getAppDirectory());
            log(Level.INFO, "* Template directory: %s", SessionManager.getDirectoriesConfig().getTemplateDirectory());
            log(Level.INFO, "* Routes directory: %s", SessionManager.getDirectoriesConfig().getAppRoutesDirectory());
            log(Level.INFO, "* Views directory: %s", SessionManager.getDirectoriesConfig().getViewsDirectory());

            log(Level.INFO, "* Environment configuration is: %s", EngineConst.getAresAppEnvironment().toUpperCase());

            SessionManager.setEnvironment(EngineConst.getAresAppEnvironment().toLowerCase());

            createDirectoryIfNotExists(SessionManager.getDirectoriesConfig().getAppDirectory());
            createDirectoryIfNotExists(SessionManager.getDirectoriesConfig().getTemplateDirectory());
            createDirectoryIfNotExists(SessionManager.getDirectoriesConfig().getAppDatabaseDirectory());
            createDirectoryIfNotExists(SessionManager.getDirectoriesConfig().getAppRoutesDirectory());
            createDirectoryIfNotExists(SessionManager.getDirectoriesConfig().getViewsDirectory());


            if (EngineConst.getAresCreateDefaultFiles())
            {

                log(Level.INFO, "- Generating files for demo mode");
                createDemoAresConfig();
                createDemoDatabaseConfig();
                createDemoRoutesConfig();
            }

            loadAresConfig();
            loadDatabaseConfig();
            loadRoutesConfig();


            return true;
        }
        catch (Exception ex)
        {

        }

        return false;
    }

    private void createDemoAresConfig() throws Exception
    {
        AresConfig config = new AresConfig();


        config.addEntry("myVar", "value");

        config.getRoutes().getStaticRoutes().add(new AresStaticRouteEntry("/imgs/.*", "imgs/", ""));
        config.getRoutes().getStaticRoutes().add(new AresStaticRouteEntry("/css/.*", "less/", LessResourceProcessor.class.getName()));


        String json = JsonSerializer.Serialize(config);

        saveFileIfNotExists(SessionManager.getDirectoriesConfig().getAppConfigDirectory() + EngineConst.DEFAULT_CONFIG_FILENAME, json);
    }

    private void createDemoDatabaseConfig() throws Exception
    {
        AresDatabaseConfigList config = new AresDatabaseConfigList();

        AresDatabaseConfig dbConfig = new AresDatabaseConfig();
        dbConfig.setEnvironment("development");
        dbConfig.setDriverClass("org.h2.Driver");
        dbConfig.setUrl("jdbc:h2:~/test_db");
        dbConfig.setUsername("test");
        dbConfig.setPassword("test");

        config.getEntries().add(dbConfig);

        dbConfig = new AresDatabaseConfig();
        dbConfig.setEnvironment("production");
        dbConfig.setDriverClass("org.postgresql.Driver");
        dbConfig.setUrl("jdbc:postgresql://localhost/test_db");
        dbConfig.setUsername("postgres");
        dbConfig.setPassword("postgres");

        config.getEntries().add(dbConfig);

        String json = JsonSerializer.Serialize(config);



        saveFileIfNotExists(SessionManager.getDirectoriesConfig().getAppDatabaseDirectory() + EngineConst.DEFAULT_DB_CONFIG_FILENAME, json);

    }

    private void saveFileIfNotExists(String filename, String data) throws Exception
    {
        if (!new File(filename).exists())
        {
            FileUtils.writeStringToFile(new File(filename), data);

        }
    }

    private void createDemoRoutesConfig() throws Exception
    {
        AresRoutes config = new AresRoutes();

        config.getRoutes().add(new AresRouteEntry(RequestType.GET, "/about.html", "testaction", "doAbout"));

        String json = JsonSerializer.Serialize(config);


        saveFileIfNotExists(SessionManager.getDirectoriesConfig().getAppRoutesDirectory() + EngineConst.DEFAULT_ROUTES_CONFIG_FILENAME, json);


    }

    private void createDirectoryIfNotExists(String directory)
    {
        if (!new File(directory).exists()) {
            log(Level.INFO, "Creating directory %s", directory);
            new File(directory).mkdirs();
        }
    }

    private void loadAresConfig() throws Exception
    {
        String json = fileToString(SessionManager.getDirectoriesConfig().getAppConfigDirectory() + EngineConst.DEFAULT_CONFIG_FILENAME);

        SessionManager.setConfig(JsonSerializer.Deserialize(json, AresConfig.class));

        log(Level.INFO, "Config is OK");
    }

    private void loadDatabaseConfig() throws Exception
    {
        String json = fileToString(SessionManager.getDirectoriesConfig().getAppDatabaseDirectory() + EngineConst.DEFAULT_DB_CONFIG_FILENAME);

        AresDatabaseConfigList list = JsonSerializer.Deserialize(json, AresDatabaseConfigList.class);

        SessionManager.setDatabaseConfig(list.getEntries().stream().filter(s -> s.getEnvironment().toLowerCase().equals(SessionManager.getEnvironment())).findFirst().get());

        log(Level.INFO, "Database config is OK");
    }

    private void loadRoutesConfig() throws Exception
    {
        String json = fileToString(SessionManager.getDirectoriesConfig().getAppRoutesDirectory() + EngineConst.DEFAULT_ROUTES_CONFIG_FILENAME);

        SessionManager.setRoutes(JsonSerializer.Deserialize(json, AresRoutes.class));

        log(Level.INFO, "Routes config is OK");
    }

    private String fileToString(String filename) throws IOException
    {
        return FileUtils.readFileToString(new File(filename));
    }

    protected void log(Level level, String text, Object ... args)
    {
        logger.log(level, String.format(text, args));
    }
}

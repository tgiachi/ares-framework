package com.github.tgiachi.ares.engine.filesystem;

import com.github.tgiachi.ares.annotations.AresFileSystemManager;
import com.github.tgiachi.ares.data.template.DataModel;
import com.github.tgiachi.ares.data.template.TemplateResult;
import com.github.tgiachi.ares.interfaces.fs.IFileSystemManager;
import com.github.tgiachi.ares.sessions.SessionManager;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.StrongCacheStorage;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.Version;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;

/**
 * Filesystem manager default
 */
@AresFileSystemManager
public class FileSystemManager implements IFileSystemManager {


    private static Logger logger = Logger.getLogger(FileSystemManager.class);


    private Configuration mTemplateConfiguration;
    private MultiTemplateLoader mMultiTemplateLoader;

    @Override
    public void start() {

        checkTemplateDirectory();
        initTemplateEngine();
    }

    private void checkTemplateDirectory()
    {
       if (!new File(SessionManager.getDirectoriesConfig().getTemplateDirectory()).exists())
       {
            new File(SessionManager.getDirectoriesConfig().getTemplateDirectory()).mkdirs();

           logger.info(String.format("Created %s templates directory", SessionManager.getDirectoriesConfig().getTemplateDirectory()));


       }
    }

    private void initTemplateEngine()
    {
        try {
            mTemplateConfiguration = new Configuration();
            mMultiTemplateLoader = new MultiTemplateLoader(new TemplateLoader[] {});

            mTemplateConfiguration.setCacheStorage(new StrongCacheStorage());

            mTemplateConfiguration.setTemplateLoader(mMultiTemplateLoader);

            // mTemplateConfiguration.setDirectoryForTemplateLoading(new File(SessionManager.getDirectoriesConfig().getTemplateDirectory()));

            log(Level.INFO, "Template engine is ready!");

        }
        catch (Exception ex)
        {
           log(Level.FATAL, "Error during init template manager => %s", ex.getMessage());
        }
    }

    @Override
    public void dispose() {

    }

    protected void log(Level level, String text, Object ... args)
    {
        logger.log(level, String.format(text, args));
    }

    @Override
    public TemplateResult getTemplate(String filename, DataModel model) {

        TemplateResult result = new TemplateResult();
        try
        {

            StringWriter sw = new StringWriter();
            Template template = mTemplateConfiguration.getTemplate(filename);
            template.process(model.getDataMap(), sw );
            result.setResult(sw.toString().getBytes());
        }
        catch (TemplateException ex)
        {
            log(Level.FATAL, "Error during get template %s => %s", filename, ex.getMessage());
            result.setError(true);
            result.setErrorString(ex.getMessage());
        }
        catch (IOException ex2)
        {
            log(Level.FATAL, "Error during get file %s => %s", filename, ex2.getMessage());
            result.setErrorString(ex2.getMessage());
        }

        return result;
    }
}

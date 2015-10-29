package com.github.tgiachi.ares.engine.filesystem;

import com.github.tgiachi.ares.annotations.AresFileSystemManager;
import com.github.tgiachi.ares.interfaces.fs.IFileSystemManager;
import com.github.tgiachi.ares.sessions.SessionManager;
import org.apache.log4j.Logger;

import java.io.File;

/**
 * Filesystem manager default
 */
@AresFileSystemManager
public class FileSystemManager implements IFileSystemManager {


    private static Logger logger = Logger.getLogger(FileSystemManager.class);



    @Override
    public void start() {

        checkTemplateDirectory();
    }

    private void checkTemplateDirectory()
    {
       if (!new File(SessionManager.getDirectoriesConfig().getTemplateDirectory()).exists())
       {
            new File(SessionManager.getDirectoriesConfig().getTemplateDirectory()).mkdirs();

           logger.info(String.format("Created %s templates directory", SessionManager.getDirectoriesConfig().getTemplateDirectory()));

       }
    }

    @Override
    public void dispose() {

    }
}

package com.github.tgiachi.ares.engine.processors.base;

import com.github.tgiachi.ares.annotations.container.AresInject;
import com.github.tgiachi.ares.data.actions.ServletResult;
import com.github.tgiachi.ares.interfaces.processors.IAresProcessor;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.nio.file.*;
import java.util.HashMap;

/**
 * Classe base per i processori di risorse
 */
public class BaseResourceProcessor implements IAresProcessor {

    @AresInject
    private Logger logger;

    private HashMap<String, WatchKey> mWatchers = new HashMap<>();

    private WatchService mWatchService;
    private Thread mWatchThread;

    public BaseResourceProcessor()
    {
        try
        {
            mWatchService = FileSystems.getDefault().newWatchService();
            mWatchThread = new Thread(this::watchRunnable);
            mWatchThread.start();
        }
        catch (Exception ex)
        {

        }

      // logger = Logger.getLogger(getClass());
    }

    @Override
    public ServletResult parse(String action, String urlMap, String directory) {
        return null;
    }

    protected void log(Level level, String text, Object ... args)
    {
        logger.log(level, String.format(text, args));
    }


    protected void addDirectoryWatch(String directory)
    {
        try
        {
            if (mWatchers.get(directory) == null) {

                final Path path = FileSystems.getDefault().getPath(directory);
                final WatchKey watchKey = path.register(mWatchService, StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_CREATE);

                mWatchers.put(directory, watchKey);
            }

        }
        catch (Exception ex)
        {
            log(Level.FATAL, "Error during add directory %s to watchers => %s",directory,ex.getMessage());
        }
    }

    private void watchRunnable()
    {
        while (true)
        {
            for(String dir: mWatchers.keySet())
            {
                WatchKey service = mWatchers.get(dir);
                for(WatchEvent<?> event : service.pollEvents())
                {
                    Path p =(Path)event.context();
                    fileChange(dir + p.getFileName().toString());
                    service.reset();
                }
            }
        }
    }

    protected void fileChange(String filename)
    {

    }
}

package com.github.tgiachi.ares.engine.processors;

import com.asual.lesscss.LessEngine;
import com.github.tgiachi.ares.annotations.container.AresResourcesProcessor;
import com.github.tgiachi.ares.data.actions.ServletResult;
import com.github.tgiachi.ares.data.debug.GenerationStat;
import com.github.tgiachi.ares.engine.processors.base.BaseResourceProcessor;
import com.github.tgiachi.ares.interfaces.processors.IAresProcessor;
import com.github.tgiachi.ares.sessions.SessionManager;
import com.google.common.base.Stopwatch;
import com.google.common.base.Strings;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * Processor per generare i file LESS
 */
@AresResourcesProcessor
public class LessResourceProcessor extends BaseResourceProcessor {



    private HashMap<String, String> mLessCache = new HashMap<>();

    private LessEngine mLessEngine;

    public LessResourceProcessor()
    {
        super();
        mLessEngine = new LessEngine();

    }



    @Override
    public ServletResult parse(String action, String urlMap, String directory) {

        ServletResult result = new ServletResult();

        log(Level.DEBUG, "Request action %s urlMap %s directory %s", action,urlMap, directory);

        try
        {
            String templateDirectory = SessionManager.getDirectoriesConfig().getTemplateDirectory() + File.separator;

            if (!directory.startsWith("/"))
                directory = templateDirectory + directory;

            urlMap = urlMap.replace("*","").replace(".","");

            action = action.replace(urlMap, directory).replace(".css", ".less");

            addDirectoryWatch(directory);

            if (mLessCache.get(action) == null)
            {

                byte[] out = compileLess(action).getBytes();
                result.setResult(out);

                result.setMimeType("text/css");
                result.setReturnCode(HttpServletResponse.SC_OK);
                mLessCache.put(action, new String(out));
            }
            else
            {
                result.setMimeType("text/css");
                result.setResult(mLessCache.get(action).getBytes());
                result.setReturnCode(HttpServletResponse.SC_OK);

            }


            //urlMap = urlMap.replace("*","").replace(".","");

        }
        catch (Exception ex)
        {
            result.setReturnCode(500);
        }

        return result;
    }

    private String compileLess(String filename)
    {
        try
        {
            String result = "";
            Stopwatch sw = Stopwatch.createStarted();
            result = mLessEngine.compile(new File(filename), true);
            SessionManager.broadcastMessage("DEBUG_GENERATION", new GenerationStat(getClass(), "LESS_GENERATION", sw.elapsed(TimeUnit.MICROSECONDS), filename));


            return result;

        }
        catch (Exception ex)
        {
            log(Level.FATAL, "Error during compile %s => %s", filename, ex.getMessage());

        }

        return "";
    }

    @Override
    protected void fileChange(String filename) {
        log(Level.INFO, "File %s changed! Reloading and compile", filename);

        String compiles = compileLess(filename);

        if (!Strings.isNullOrEmpty(compiles))
        {
            mLessCache.put(filename, compiles);
        }
    }
}

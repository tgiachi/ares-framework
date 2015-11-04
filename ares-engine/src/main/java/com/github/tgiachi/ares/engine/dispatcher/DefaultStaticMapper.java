package com.github.tgiachi.ares.engine.dispatcher;

import com.github.tgiachi.ares.data.actions.ServletResult;
import com.github.tgiachi.ares.interfaces.processors.IAresProcessor;
import com.github.tgiachi.ares.sessions.SessionManager;
import org.apache.commons.io.FileUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Static mapper default
 */

public class DefaultStaticMapper implements IAresProcessor {


    @Override
    public ServletResult parse(String action,String urlMap, String directory) {


        String templateDirectory = SessionManager.getDirectoriesConfig().getTemplateDirectory() + File.separator;

        if (!directory.startsWith("/"))
            directory = templateDirectory + directory;



        urlMap = urlMap.replace("*","").replace(".","");

        action = action.replace(urlMap, directory);

        ServletResult result = new ServletResult(HttpServletResponse.SC_NOT_FOUND);



        try
        {
            File f = new File(action);
            result.setResult(FileUtils.readFileToByteArray(f));
            result.setMimeType(Files.probeContentType(Paths.get(action)));
            result.setReturnCode(HttpServletResponse.SC_OK);

        }
        catch (Exception ex)
        {

        }


        return result;

    }
}

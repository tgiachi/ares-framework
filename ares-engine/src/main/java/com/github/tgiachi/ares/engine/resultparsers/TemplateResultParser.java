package com.github.tgiachi.ares.engine.resultparsers;

import com.github.tgiachi.ares.annotations.actions.AresAction;
import com.github.tgiachi.ares.annotations.resultsparsers.AresResultParser;
import com.github.tgiachi.ares.data.actions.AresViewBag;
import com.github.tgiachi.ares.data.actions.ServletResult;
import com.github.tgiachi.ares.data.template.DataModel;
import com.github.tgiachi.ares.data.template.TemplateResult;
import com.github.tgiachi.ares.engine.resultparsers.base.BaseResultParser;
import com.github.tgiachi.ares.interfaces.actions.IAresAction;
import com.github.tgiachi.ares.sessions.SessionManager;
import com.google.common.base.Stopwatch;
import org.apache.log4j.Level;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * classe per per il parsing dei template
 */
@AresResultParser(AresViewBag.class)
public class TemplateResultParser extends BaseResultParser {

    @Override
    public ServletResult parse(DataModel model, Method method, Object invoker, Object[] params) throws Exception {
        Stopwatch stopwatch = Stopwatch.createStarted();
        AresViewBag viewBag =  (AresViewBag) method.invoke(invoker, params);

        if (viewBag.getViewPage() == null)
        {
            AresAction ann = invoker.getClass().getAnnotation(AresAction.class);
            viewBag.setViewPage(String.format("%s%s%s.ftl", ann.name(), File.separator, method.getName()));

            log(Level.INFO, "Automatic set view page to correct view file %s", viewBag.getViewPage());
        }


        TemplateResult templateResult = getEngine().getFileSystemManager().getTemplate(viewBag.getViewPage(), viewBag.getModel());
        stopwatch.stop();


        if (!templateResult.isError())
            return new ServletResult(generateDebugInfos(templateResult.getResult(), stopwatch).getBytes());
        else
        {
            ServletResult result = new ServletResult(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            result.setException(new Exception(templateResult.getErrorString()));
            return result;
        }
    }

    private String generateDebugInfos(String result, Stopwatch stopwatch)
    {
        result += String.format("<!-- Page generated in %s microseconds -->",stopwatch.elapsed(TimeUnit.MICROSECONDS));
        return result;
    }
}

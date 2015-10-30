package com.github.tgiachi.ares.engine.dispacher;

import com.github.tgiachi.ares.annotations.actions.AresAction;
import com.github.tgiachi.ares.annotations.actions.MapRequest;
import com.github.tgiachi.ares.annotations.actions.RequestType;
import com.github.tgiachi.ares.data.actions.AresViewBag;
import com.github.tgiachi.ares.data.actions.ServletResult;
import com.github.tgiachi.ares.data.db.AresQuery;
import com.github.tgiachi.ares.data.template.DataModel;
import com.github.tgiachi.ares.data.template.TemplateResult;
import com.github.tgiachi.ares.engine.reflections.ReflectionUtils;
import com.github.tgiachi.ares.interfaces.actions.IAresAction;
import com.github.tgiachi.ares.interfaces.dispacher.IAresDispacher;
import com.github.tgiachi.ares.interfaces.engine.IAresEngine;
import com.google.common.base.Stopwatch;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Sistema di dispach delle pagine di default
 */
public class DefaultDispacher implements IAresDispacher {

    private static Logger logger = Logger.getLogger(DefaultDispacher.class);

    private HashMap<String, IAresAction> mActions = new HashMap<>();

    private HashMap<MapRequest, Method> mActionMethods = new HashMap<>();

    private IAresEngine engine;

    public DefaultDispacher(IAresEngine engine)
    {
        this.engine = engine;
        log(Level.INFO, "Default dispacher is ready");

        buildActionAnnotations();

        buildActionMethods();


    }

    private void buildActionMethods()
    {
        try
        {
            for (String key : mActions.keySet())
            {
                IAresAction action = mActions.get(key);

                action.getClass().getAnnotationsByType(MapRequest.class);

                log(Level.INFO, "Building methods for action %s ", key);

                final Method[] declaredMethods = action.getClass().getDeclaredMethods();

                for(Method m : declaredMethods)
                {
                    if (m.isAnnotationPresent(MapRequest.class))
                    {
                        mActionMethods.put(m.getAnnotation(MapRequest.class), m);
                        log(Level.INFO, "%s->%s", action.getClass().getSimpleName(), m.getName());
                    }
                }

            }

        }
        catch (Exception ex)
        {
            log(Level.FATAL, "Error during build action<->methods %s",ex.getMessage());
        }

    }

    private void buildActionAnnotations()
    {
        Set<Class<?>> classes = ReflectionUtils.getAnnotation(AresAction.class);

        for (Class<?> classz : classes)
        {
             AresAction actionAnnotations = classz.getAnnotation(AresAction.class);



            try
            {
                mActions.put(actionAnnotations.name(), (IAresAction) classz.newInstance());
            }
            catch (Exception ex)
            {
                log(Level.FATAL, "Error during init action %s => %s", actionAnnotations.name(), ex.getMessage());
            }
        }
    }

    @Override
    public ServletResult dispach(String action, RequestType type, HashMap<String, String> headers, HashMap<String, String> values)
    {
        ServletResult Sresult = new ServletResult();
        Optional<MapRequest> key = mActionMethods.keySet().parallelStream().filter(s -> s.type() == type && s.path().equals(action)).findFirst();

        if (key.isPresent())
        {
            Method m = mActionMethods.get(key.get());
            DataModel model = new DataModel();
            model.addAttribute("request_type", type);
            model.addAttribute("headers", headers);
            model.addAttribute("values", values);

            try {

                AresAction aresAction = m.getDeclaringClass().getAnnotation(AresAction.class);

                IAresAction invoker = mActions.get(aresAction.name());

                if (m.getParameterTypes().length == 2)
                {

                    AresQuery query = engine.getDatabaseManager().getNewQuery();

                    Stopwatch sw = Stopwatch.createStarted();


                    AresViewBag bag = (AresViewBag) m.invoke(invoker, model, query);

                    sw.stop();

                    model.addAttribute("invoke_generation_time", sw.elapsed(TimeUnit.MICROSECONDS));



                    TemplateResult result = engine.getFileSystemManager().getTemplate(bag.getViewPage(), bag.getModel());

                    Sresult.setResult(result);

                    engine.getDatabaseManager().disposeQuery(query);
                    model.addAttribute("template_generation_time", result.getGenerationTime());


                }
                else {

                    Stopwatch sw = Stopwatch.createStarted();
                    AresViewBag bag = (AresViewBag) m.invoke(invoker, model);
                    sw.stop();

                    model.addAttribute("invoke_generation_time", sw.elapsed(TimeUnit.MICROSECONDS));

                    log(Level.INFO, "Bag is %s ", bag);

                    TemplateResult result = engine.getFileSystemManager().getTemplate(bag.getViewPage(), bag.getModel());
                    model.addAttribute("template_generation_time", result.getGenerationTime());

                    Sresult.setResult(result);
                }

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }


        }

        return Sresult;

    }

    protected void log(Level level, String text, Object ... args)
    {
        logger.log(level, String.format(text, args));
    }
}

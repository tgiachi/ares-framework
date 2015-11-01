package com.github.tgiachi.ares.engine.dispacher;

import com.github.tgiachi.ares.annotations.actions.AresAction;
import com.github.tgiachi.ares.annotations.actions.GetParam;
import com.github.tgiachi.ares.annotations.actions.MapRequest;
import com.github.tgiachi.ares.annotations.actions.RequestType;
import com.github.tgiachi.ares.annotations.resultsparsers.AresResultParser;
import com.github.tgiachi.ares.data.actions.AresViewBag;
import com.github.tgiachi.ares.data.actions.ServletResult;
import com.github.tgiachi.ares.data.db.AresQuery;
import com.github.tgiachi.ares.data.template.*;
import com.github.tgiachi.ares.engine.reflections.ReflectionUtils;
import com.github.tgiachi.ares.engine.serializer.JsonSerializer;
import com.github.tgiachi.ares.engine.serializer.XmlSerializer;
import com.github.tgiachi.ares.engine.serializer.YAMLSerializer;
import com.github.tgiachi.ares.engine.utils.AppInfo;
import com.github.tgiachi.ares.interfaces.actions.IAresAction;
import com.github.tgiachi.ares.interfaces.dispacher.IAresDispacher;
import com.github.tgiachi.ares.interfaces.engine.IAresEngine;
import com.github.tgiachi.ares.interfaces.resultsparsers.IResultParser;
import com.google.common.base.Stopwatch;
import com.sun.deploy.net.HttpRequest;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Sistema di dispach delle pagine di default
 */
public class DefaultDispacher implements IAresDispacher {

    private static Logger logger = Logger.getLogger(DefaultDispacher.class);

    private HashMap<String, IAresAction> mActions = new HashMap<>();

    private HashMap<MapRequest, Method> mActionMethods = new HashMap<>();

    private HashMap<Class<?>, IResultParser> mResultsParsers = new HashMap<>();

    private IAresEngine engine;

    public DefaultDispacher(IAresEngine engine)
    {
        this.engine = engine;
        log(Level.INFO, "Default dispacher is ready");

        buildActionAnnotations();

        buildActionMethods();

        buildResultsParsers();

        buildStaticMappers();

    }

    private void buildStaticMappers() {

    }

    private void buildResultsParsers() {

        try
        {
            Set<Class<?>> classes = ReflectionUtils.getAnnotation(AresResultParser.class);

            log(Level.INFO, "Found %s result parsers", classes.size());

            for (Class<?> classz : classes)
            {
                IResultParser parser = (IResultParser)classz.newInstance();
                AresResultParser annotation = classz.getAnnotation(AresResultParser.class);

                parser.init(engine);

                mResultsParsers.put(annotation.value(), parser);

            }

        }
        catch (Exception ex)
        {
            log(Level.FATAL, "Error during scan results Parsers => %s", ex.getMessage());
        }

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
    public ServletResult dispach(String action, RequestType type, HashMap<String, String> headers, HashMap<String, String> values, HttpServletRequest request)
    {
        ServletResult servletResult = new ServletResult();

        Optional<MapRequest> key = mActionMethods.keySet().parallelStream().filter(s -> s.type() == type && s.path().equals(action)).findFirst();

        if (key.isPresent()) {
            Method m = mActionMethods.get(key.get());
            DataModel model = new DataModel();
            model.addAttribute("request_type", type);
            model.addAttribute("headers", headers);
            model.addAttribute("values", values);
            model.addAttribute("invoke_generation_time", 0);
            model.addAttribute("gitproperties", AppInfo.gitProperties);
            model.addAttribute("appname", AppInfo.AppName);
            model.addAttribute("appversion", AppInfo.AppVersion);
            model.addAttribute("session", request.getSession());


            try {

                AresAction aresAction = m.getDeclaringClass().getAnnotation(AresAction.class);
                AresQuery query;

                IAresAction invoker = mActions.get(aresAction.name());
                engine.getContainer().resolveWires(invoker);

                List<Object> invokerParams = new ArrayList<>();

                for (Parameter c : m.getParameters()) {


                    if (c.getType().equals(DataModel.class))
                        invokerParams.add(model);
                    else if (c.getType().equals(AresQuery.class)) {
                        query = engine.getDatabaseManager().getNewQuery();
                        invokerParams.add(query);
                    }
                    else if (c.getType().equals(HttpSession.class))
                        invokerParams.add(request.getSession());

                    else if (c.isAnnotationPresent(GetParam.class)) {

                        GetParam annotation = c.getAnnotation(GetParam.class);
                        invokerParams.add(values.get(annotation.value()));
                    }

                }



                Optional<Class<?>> resultKey = (mResultsParsers.keySet().parallelStream().filter(s -> s.equals(m.getReturnType())).findFirst());

                if (resultKey.isPresent()) {
                    IResultParser parser = mResultsParsers.get(resultKey.get());
                    engine.getContainer().resolveWires(parser);


                    try {
                        servletResult = parser.parse(model, m, invoker, invokerParams.toArray());
                    } catch (Exception ex) {
                        log(Level.FATAL, "Error during call result parsers %s ==> %s", parser.getClass(), ex.getMessage());
                    }


                }
            }
            catch (Exception ex)
            {

            }
        }
        else
        {
            servletResult.setErrorCode(404);
        }




        return servletResult;

    }

    protected void log(Level level, String text, Object ... args)
    {
        logger.log(level, String.format(text, args));
    }
}

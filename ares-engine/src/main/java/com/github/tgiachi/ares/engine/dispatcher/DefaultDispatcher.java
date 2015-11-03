package com.github.tgiachi.ares.engine.dispatcher;

import com.github.tgiachi.ares.annotations.actions.*;
import com.github.tgiachi.ares.annotations.container.AresResourcesProcessor;
import com.github.tgiachi.ares.annotations.resultsparsers.AresResultParser;
import com.github.tgiachi.ares.data.actions.ServletResult;
import com.github.tgiachi.ares.data.config.AresRouteEntry;
import com.github.tgiachi.ares.data.db.AresQuery;
import com.github.tgiachi.ares.data.template.*;
import com.github.tgiachi.ares.utils.ReflectionUtils;
import com.github.tgiachi.ares.engine.utils.AppInfo;
import com.github.tgiachi.ares.engine.utils.EngineConst;
import com.github.tgiachi.ares.interfaces.actions.IAresAction;
import com.github.tgiachi.ares.interfaces.dispacher.IAresDispatcher;
import com.github.tgiachi.ares.interfaces.engine.IAresEngine;
import com.github.tgiachi.ares.interfaces.processors.IAresProcessor;
import com.github.tgiachi.ares.interfaces.resultsparsers.IResultParser;
import com.github.tgiachi.ares.sessions.SessionManager;
import com.google.common.base.Strings;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Sistema di dispatch delle pagine di default
 */
public class DefaultDispatcher implements IAresDispatcher {

    private static Logger logger = Logger.getLogger(DefaultDispatcher.class);

    private UrlMapperRouter mapperRouter = new UrlMapperRouter();

    private HashMap<Class<?>, IResultParser> mResultsParsers = new HashMap<>();

    private DefaultStaticMapper defaultStaticMapper;

    private IAresEngine engine;

    private List<IAresProcessor> resourcesProcessors = new ArrayList<>();

    private HashMap<Pattern, AresRouteEntry> mStaticRouters = new HashMap<>();

    private HashMap<String, IAresProcessor> mProcessors = new HashMap<>();

    public DefaultDispatcher(IAresEngine engine)
    {
        this.engine = engine;
        log(Level.INFO, "Default dispatcher is ready");

        buildActionAnnotations();

        buildResultsParsers();

        buildResourcesProcessors();

        buildStaticMappers();

    }

    private void buildResourcesProcessors()
    {

        Set<Class<?>> classes = ReflectionUtils.getAnnotation(AresResourcesProcessor.class);

        for (Class<?> classz : classes)
        {
            try
            {
                IAresProcessor processor = (IAresProcessor)classz.newInstance();

                engine.getContainer().resolveWires(processor);

                mProcessors.put(classz.getName(), processor);

            }
            catch (Exception ex)
            {
                log(Level.FATAL, "Error during load Resource Processor class %s => %s", classz.getSimpleName(), ex.getMessage());
            }
        }
    }

    private void buildStaticMappers() {
        try
        {
            for (AresRouteEntry entry : SessionManager.getConfig().getRoutes().getStaticRoutes())
            {

                log(Level.INFO, " Mapping %s -> %s", entry.getUrlMap(), entry.getDirectory());

                mStaticRouters.put(Pattern.compile(entry.getUrlMap()), entry);
            }


            defaultStaticMapper = new DefaultStaticMapper();
        }
        catch (Exception ex)
        {
            log(Level.FATAL, "Error during build static mapper : %s", ex.getMessage());
        }

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


    private void buildActionAnnotations()
    {
        Set<Class<?>> classes = ReflectionUtils.getAnnotation(AresAction.class);

        for (Class<?> classz : classes)
        {
             AresAction actionAnnotations = classz.getAnnotation(AresAction.class);

            try
            {
                IAresAction action = (IAresAction)classz.newInstance();

                engine.getContainer().resolveWires(action);

                for(Method m : classz.getDeclaredMethods())
                {
                    if (m.isAnnotationPresent(MapRequest.class))
                    {
                        log(Level.DEBUG, "Found @MapRequest annotation on action %s", classz.getName());
                        MapRequest mapRequest = m.getAnnotation(MapRequest.class);

                        try
                        {
                            mapperRouter.addMap(actionAnnotations.baseUrl() + mapRequest.path(), action);
                        }
                        catch (Exception ex)
                        {
                            log(Level.FATAL, "Error during create instance of action %s ==> %s", classz.getName(), ex.getMessage());
                        }
                    }
                }


             //   mActions.put(actionAnnotations.name(), (IAresAction) classz.newInstance());
            }
            catch (Exception ex)
            {
                log(Level.FATAL, "Error during init action %s => %s", actionAnnotations.name(), ex.getMessage());
            }
        }
    }

    public ServletResult checkStaticResource(String request)
    {

        Optional<Pattern> key =mStaticRouters.keySet().stream().filter(s-> s.matcher(request).matches()).findAny();

        if (key.isPresent())
        {

            AresRouteEntry staticEntry = mStaticRouters.get(key.get());

            if (Strings.isNullOrEmpty(staticEntry.getProcessorClass()))
            {
                ServletResult result = defaultStaticMapper.parse(request, staticEntry.getUrlMap(), staticEntry.getDirectory());

                return result;
            }
            else
            {
                IAresProcessor processor = mProcessors.get(staticEntry.getProcessorClass());

                if (processor != null)
                {
                    ServletResult result = processor.parse(request, staticEntry.getUrlMap(), staticEntry.getDirectory());
                    return result;
                }
            }
            
        }

        return new ServletResult(HttpServletResponse.SC_NOT_FOUND);
    }

    public DataModel prepareDefaultDatamodel(String action, RequestType type, HashMap<String, String> headers, HashMap<String, String> values, HttpServletRequest request)
    {
        DataModel model = new DataModel();

        model.addAttribute(EngineConst.MODEL_VAR_ACTION, action);
        model.addAttribute(EngineConst.MODEL_VAR_REQUEST_TYPE, type);
        model.addAttribute(EngineConst.MODEL_VAR_HEADERS, headers);
        model.addAttribute(EngineConst.MODEL_VAR_VALUES, values);
        model.addAttribute(EngineConst.MODEL_VAR_INVOKE_GENERATOR_TIME, 0);
        model.addAttribute(EngineConst.MODEL_VAR_GIT_PROPERTIES, AppInfo.gitProperties);
        model.addAttribute(EngineConst.MODEL_APP_NAME, AppInfo.AppName);
        model.addAttribute(EngineConst.MODEL_APP_VERSION, AppInfo.AppVersion);
        model.addAttribute(EngineConst.MODEL_SESSION, request.getSession());
        model.addAttribute(EngineConst.MODEL_CONTEXT_PATH, request.getContextPath() + "/");
        return model;

    }

    @Override
    public ServletResult dispatch(String action, RequestType type, HashMap<String, String> headers, HashMap<String, String> values, HttpServletRequest request)
    {
        IAresAction aresAction = mapperRouter.match(action);
        ServletResult servletResult = new ServletResult(HttpServletResponse.SC_NOT_FOUND);
        boolean actionExecuted = false;


        if (aresAction != null)
        {
            DataModel model = prepareDefaultDatamodel(action, type,headers,values,request);

            if (!needAuth(aresAction))
            {
                servletResult = callActionMethod(aresAction, action, type, request, model, values);

                if (servletResult != null)
                    actionExecuted = true;
            }
            else
            {
                if (!getSessionValue(request.getSession(), EngineConst.SESSION_USER_AUTHENTICATED).equals("true"))
                {
                    servletResult = new ServletResult(HttpServletResponse.SC_MOVED_PERMANENTLY);
                    setSessionValue(request.getSession(), EngineConst.SESSION_PRE_AUTH, action);
                    servletResult.setResult("http://localhost:8080/auth/login".getBytes());

                    actionExecuted = true;
                }
                else
                {
                    servletResult = callActionMethod(aresAction, action, type, request, model, values);

                    if (servletResult != null)
                        actionExecuted = true;

                }
            }
        }

        if (!actionExecuted)
        {
            servletResult = checkStaticResource(action);
        }


        log(Level.INFO, "[%s] - %s - %s - %s", request.getRemoteAddr(), type, servletResult.getReturnCode(), action);

        return servletResult;

    }

    private String getSessionValue(HttpSession session, String key)
    {
        if (session.getAttribute(key) != null)
            return (String)session.getAttribute(key);
        else
            return "";

    }

    private void setSessionValue(HttpSession session , String key, String value)
    {
        session.setAttribute(key,value);
    }

    private boolean needAuth(IAresAction aresAction)
    {
        return (aresAction.getClass().isAnnotationPresent(AresProtectedArea.class));
    }

    private ServletResult callActionMethod( IAresAction aresAction, String action, RequestType type, HttpServletRequest request, DataModel model, HashMap<String,String> values)
    {
        Method m = getActionMethod(aresAction, action, type);

        if (m != null)
        {
            List<Object> invokerParams = buildParams(m,request,model,values);

            Optional<Class<?>> resultKey = (mResultsParsers.keySet().parallelStream().filter(s -> s.equals(m.getReturnType())).findFirst());

            if (resultKey.isPresent()) {
                IResultParser parser = mResultsParsers.get(resultKey.get());
                engine.getContainer().resolveWires(parser);

                try
                {
                    ServletResult result = parser.parse(model, m, aresAction, invokerParams.toArray());

                    setSessionValue(request.getSession(), EngineConst.SESSION_PREV_URL, action);

                    return result;

                } catch (Exception ex) {
                    log(Level.FATAL, "Error during call result parsers %s ==> %s", parser.getClass(), ex.getMessage());
                }


            }
        }



        return null;

    }

    private List<Object> buildParams(Method m, HttpServletRequest request, DataModel model, HashMap<String, String> values)
    {
        List<Object> invokerParams = new ArrayList<>();

        //Costruisce l'invocatore
        for(Parameter c : m.getParameters())
        {
            if (c.getType().equals(DataModel.class))
                invokerParams.add(model);
            else if (c.getType().equals(AresQuery.class)) {

                invokerParams.add(engine.getDatabaseManager().getNewQuery());
            }
            else if (c.getType().equals(HttpSession.class))
                invokerParams.add(request.getSession());

            else if (c.isAnnotationPresent(GetParam.class)) {

                GetParam annotation = c.getAnnotation(GetParam.class);

                if (values.get(annotation.value()) != null)
                    invokerParams.add(values.get(annotation.value()));
                else
                    invokerParams.add("");
            }
            else if (c.isAnnotationPresent(GetSessionParam.class))
            {
                GetSessionParam annotation = c.getAnnotation(GetSessionParam.class);

                invokerParams.add(getSessionValue(request.getSession(), annotation.value()));
            }

        }

        return invokerParams;

    }

    private Method getActionMethod(IAresAction aresAction,String action, RequestType type)
    {
        Method m = null;
        AresAction actionAnnotation = aresAction.getClass().getAnnotation(AresAction.class);
        Pattern p1 = Pattern.compile(action);

        for(Method method : aresAction.getClass().getDeclaredMethods())
        {
            if (method.isAnnotationPresent(MapRequest.class))
            {
                MapRequest annotation = method.getAnnotation(MapRequest.class);

                Pattern p2 = Pattern.compile(actionAnnotation.baseUrl() + annotation.path());

                if (p1.matcher(p2.pattern()).matches() && annotation.type() == type)
                {
                    m = method;
                    break;
                }
            }
        }


        return m;
    }

    protected void log(Level level, String text, Object ... args)
    {
        logger.log(level, String.format(text, args));
    }
}

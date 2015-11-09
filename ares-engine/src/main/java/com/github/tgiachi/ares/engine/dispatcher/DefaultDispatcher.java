package com.github.tgiachi.ares.engine.dispatcher;

import com.github.tgiachi.ares.annotations.actions.*;
import com.github.tgiachi.ares.annotations.container.AresResourcesProcessor;
import com.github.tgiachi.ares.annotations.resultsparsers.AresResultParser;
import com.github.tgiachi.ares.chain.manager.HeaderChainProcessor;
import com.github.tgiachi.ares.data.actions.ServletResult;
import com.github.tgiachi.ares.data.config.AresRouteEntry;
import com.github.tgiachi.ares.data.config.AresStaticRouteEntry;
import com.github.tgiachi.ares.data.db.AresQuery;
import com.github.tgiachi.ares.data.template.DataModel;
import com.github.tgiachi.ares.engine.utils.AppInfo;
import com.github.tgiachi.ares.engine.utils.EngineConst;
import com.github.tgiachi.ares.interfaces.actions.IAresAction;
import com.github.tgiachi.ares.interfaces.chain.IChainProcessor;
import com.github.tgiachi.ares.interfaces.database.IOrmManager;
import com.github.tgiachi.ares.interfaces.dispacher.IAresDispatcher;
import com.github.tgiachi.ares.interfaces.engine.IAresEngine;
import com.github.tgiachi.ares.interfaces.processors.IAresProcessor;
import com.github.tgiachi.ares.interfaces.resultsparsers.IResultParser;
import com.github.tgiachi.ares.sessions.SessionManager;
import com.github.tgiachi.ares.utils.ReflectionUtils;
import com.google.common.base.Strings;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import javax.servlet.http.Cookie;
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

    private HashMap<Pattern, AresStaticRouteEntry> mStaticRouters = new HashMap<>();

    private HashMap<String, IAresProcessor> mProcessors = new HashMap<>();

    private HashMap<Integer, IAresAction> mCodesResultMethod = new HashMap<>();

    private HeaderChainProcessor headerChainProcessor;

    public DefaultDispatcher(IAresEngine engine)
    {
        this.engine = engine;
        log(Level.INFO, "Default dispatcher is ready");

        buildActionAnnotations();

        buildConfigRoutes();

        buildResultsParsers();

        buildResourcesProcessors();

        buildStaticMappers();

        buildHeaderChainProcessors();

    }

    @Override
    public String getAction(String action) {
        ActionInfo actionInfo = mapperRouter.resolveActionString(action);

        return actionInfo.getFullUrl();
    }

    @Override
    public HashMap<String, ActionInfo> getActionsMap()
    {
        return mapperRouter.getActionMap();
    }
    private void buildHeaderChainProcessors() {
        try
        {
            headerChainProcessor = new HeaderChainProcessor();
            Set<Class<?>> classes = ReflectionUtils.getAnnotation(HeaderProcessor.class);

            log(Level.INFO, "Found %s header processors", classes.size());

            for (Class<?> classz : classes)
            {

                IChainProcessor chainProcessor = (IChainProcessor) classz.newInstance();
                engine.getContainer().resolveWires(chainProcessor);
                headerChainProcessor.addToChain(chainProcessor);


            }

        }
        catch (Exception ex)
        {

        }
    }

    private void buildConfigRoutes() {

        log(Level.INFO, "Building config routes");
        for(AresRouteEntry entry : SessionManager.getRoutes().getRoutes())
        {
            IAresAction action = mapperRouter.getActionByName(entry.getActionName());

            if (action != null)
            {
                log(Level.INFO, "Adding %s.%s -> %s - %s", entry.getActionName(), entry.getMethod(), entry.getType(), entry.getMap());
                mapperRouter.addMap(entry.getMap(), action, entry);
            }
        }
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
            for (AresStaticRouteEntry entry : SessionManager.getConfig().getRoutes().getStaticRoutes())
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

                            if (m.isAnnotationPresent(AresCodeResult.class))
                            {
                                AresCodeResult codeAnnotation = m.getAnnotation(AresCodeResult.class);

                                log(Level.INFO, "Found method for error %s => %s", codeAnnotation.value(), m.getName());


                                mCodesResultMethod.put(codeAnnotation.value(), action );
                            }
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

            AresStaticRouteEntry staticEntry = mStaticRouters.get(key.get());

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

    private ServletResult resolveCodes(int code, ServletResult result, String action, RequestType type, HashMap<String, String> headers, HashMap<String, String> values, HttpServletRequest request)
    {
        IAresAction aresAction = mCodesResultMethod.get(code);
        DataModel model = prepareDefaultDatamodel(action,type,headers,values,request);

        if (result.getException() != null)
        {
            model.addAttribute("exception", result.getException());
        }

        if (aresAction != null)
        {
            Method codeMethod = null;
            for(Method m : aresAction.getClass().getDeclaredMethods())
            {
                if (m.isAnnotationPresent(AresCodeResult.class))
                {
                    AresCodeResult annotation = m.getAnnotation(AresCodeResult.class);

                    if (annotation.value() == code)
                    {
                       codeMethod = m;
                    }
                }
            }

            if (codeMethod != null)
            {
                result = callActionMethod(codeMethod, aresAction, action, type, request, model, values);
            }
        }

        return result;

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
        model.addAttribute(EngineConst.MODEL_SESSION_MAP, getSessionHashMap(request));
        model.addAttribute(EngineConst.MODEL_CONTEXT_PATH, request.getContextPath() + "/");
        model.addAttribute(EngineConst.MODEL_ENVIRONMENT, SessionManager.getEnvironment());

        return model;

    }

    @Override
    public ServletResult dispatch(String action, RequestType type, HashMap<String, String> headers, HashMap<String, String> values, HttpServletRequest request)
    {
        IAresAction aresAction = mapperRouter.match(action);
        ServletResult servletResult = new ServletResult(HttpServletResponse.SC_NOT_FOUND);
        boolean actionExecuted = false;



        SessionManager.debug(this, 200, request ,Level.INFO, action, type, "Requesting url: %s", action);

        if (aresAction != null)
        {
            DataModel model = prepareDefaultDatamodel(action, type,headers,values,request);

            if (!needAuth(aresAction))
            {
                servletResult = callActionMethod(aresAction, action, type, request, model, values);


                if (servletResult != null) {
                    actionExecuted = true;

                    if (servletResult.getResult() != null)
                    SessionManager.debug(this, servletResult.getReturnCode(), request, Level.INFO, action, type, "Action found with total result size %s ", FileUtils.byteCountToDisplaySize(servletResult.getResult().length));

                }
            }
            else
            {
                if (!getSessionValue(request.getSession(), EngineConst.SESSION_USER_AUTHENTICATED).equals("true"))
                {
                    servletResult = new ServletResult(HttpServletResponse.SC_MOVED_PERMANENTLY);
                    servletResult.setResult(mapperRouter.resolveActionString(SessionManager.getConfig().getAuthMap().getLoginAction()).getFullUrl().getBytes());

                    setSessionValue(request.getSession(), EngineConst.SESSION_PRE_AUTH, action);

                    SessionManager.debug(this,servletResult.getReturnCode(), request, Level.INFO, action, type, "Session need auth for navitate to url %s", action);

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
        if (!actionExecuted )
        {
            AresRouteEntry routeEntry = mapperRouter.getConfigRouter(action);

            if (routeEntry != null)
            {
                if (routeEntry.getType() == type)
                {
                    DataModel model = prepareDefaultDatamodel(action, type, headers, values, request);
                    Method m =  getActionMethod(routeEntry.getMethod(), aresAction, action, type);

                    servletResult = callActionMethod(m, aresAction,action,type,request,model,values);
                    actionExecuted = true;
                }
            }

        }


        if (servletResult.getException() != null)
        {
            if (servletResult != null)
                SessionManager.debug(this, servletResult.getReturnCode(),request, Level.ERROR, action, type, "User server error <code>%s</code>", servletResult.getException());

            servletResult = resolveCodes(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, servletResult, action, type,headers,values,request);



        }

        if (!actionExecuted)
        {
            servletResult = checkStaticResource(action);
        }


        if (servletResult.getReturnCode() == HttpServletResponse.SC_NOT_FOUND)
        {
            servletResult = resolveCodes(HttpServletResponse.SC_NOT_FOUND, servletResult, action, type,headers,values,request);
        }


        log(Level.INFO, "[%s] - %s - %s - %s", request.getRemoteAddr(), type, servletResult.getReturnCode(), action);

        return servletResult;

    }

    private String getLoginUrl()
    {
        IAresAction action = mapperRouter.getActionByName("authorize");
        if (action != null)
        {
            for (Method m : action.getClass().getDeclaredMethods())
            {
                if (m.isAnnotationPresent(MapRequest.class))
                {
                    MapRequest mapRequest = m.getAnnotation(MapRequest.class);

                   // if (mapRequest.)
                }
            }

        }

        return "";
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
        session.setAttribute(key, value);
    }

    private boolean needAuth(IAresAction aresAction)
    {
        return (aresAction.getClass().isAnnotationPresent(AresProtectedArea.class));
    }
    private ServletResult callActionMethod(IAresAction aresAction, String action, RequestType type, HttpServletRequest request, DataModel model, HashMap<String,String> values)
    {
        return callActionMethod(null, aresAction,action,type,request,model,values);
    }

    private ServletResult callActionMethod( Method m, IAresAction aresAction, String action, RequestType type, HttpServletRequest request, DataModel model, HashMap<String,String> values)
    {
         if (m == null)
             m = getActionMethod(aresAction, action, type);

        if (m != null)
        {
            List<Object> invokerParams = buildParams(m,request,model,values);

            final Method finalM = m;
            Optional<Class<?>> resultKey = (mResultsParsers.keySet().parallelStream().filter(s -> s.equals(finalM.getReturnType())).findFirst());

            if (resultKey.isPresent()) {
                IResultParser parser = mResultsParsers.get(resultKey.get());
                engine.getContainer().resolveWires(parser);

                SessionManager.debug(this, 200, request,Level.INFO, action, type, "Found parser => %s", parser.getClass().getName());

                try
                {
                    ServletResult result = parser.parse(model, m, aresAction, invokerParams.toArray());
                    headerChainProcessor.executeAfterChainProcessor(result);

                    if (result.getResult() != null)
                        SessionManager.debug(parser, 200, request,Level.INFO, action, type, "Result is %s and Mime type: %s",FileUtils.byteCountToDisplaySize(result.getResult().length) , result.getMimeType());


                    setSessionValue(request.getSession(), EngineConst.SESSION_PREV_URL, action);

                    return result;

                } catch (Exception ex) {
                    log(Level.FATAL, "Error during call result parsers %s ==> %s", parser.getClass(), ex.getMessage());

                    ServletResult result = new ServletResult(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    result.setException(ex);
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
            else if (c.isAnnotationPresent(GetCookie.class))
            {
               GetCookie annotation = c.getAnnotation(GetCookie.class);
                boolean found = false;

                for (Cookie cookie : request.getCookies())
                {
                    if (cookie.getName().equals(annotation.value()))
                    {
                        invokerParams.add(cookie);
                        found = true;
                        break;
                    }


                }

                if (!found)
                    invokerParams.add(null);
            }
            else if (c.isAnnotationPresent(GetSessionParam.class))
            {
                GetSessionParam annotation = c.getAnnotation(GetSessionParam.class);

                invokerParams.add(getSessionValue(request.getSession(), annotation.value()));
            }
            else if (c.getType().equals(IOrmManager.class))
            {
                if (engine.getDatabaseManager() instanceof IOrmManager)
                    invokerParams.add(engine.getDatabaseManager());
                else
                    invokerParams.add(null);
            }

        }

        return invokerParams;

    }

    private Method getActionMethod(String methodName, IAresAction aresAction, String action, RequestType type )
    {

        for(Method method : aresAction.getClass().getDeclaredMethods())
        {
            if (method.getName().equals(methodName))
                return method;
        }

        return null;
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

    public HashMap<String, Object> getSessionHashMap(HttpServletRequest request)
    {
        HashMap<String, Object> session = new HashMap<>();

        for(String key : Collections.list(request.getSession().getAttributeNames()))
        {
            session.put(key, request.getSession().getAttribute(key));
        }

        return session;
    }

    protected void log(Level level, String text, Object ... args)
    {
        logger.log(level, String.format(text, args));
    }


}

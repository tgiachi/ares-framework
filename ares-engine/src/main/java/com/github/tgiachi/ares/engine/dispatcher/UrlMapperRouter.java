package com.github.tgiachi.ares.engine.dispatcher;

import com.github.tgiachi.ares.annotations.actions.ActionInfo;
import com.github.tgiachi.ares.annotations.actions.AresAction;
import com.github.tgiachi.ares.annotations.actions.MapRequest;
import com.github.tgiachi.ares.data.config.AresRouteEntry;
import com.github.tgiachi.ares.interfaces.actions.IAresAction;
import com.google.common.base.Strings;
import org.apache.log4j.Logger;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Classe per il parse delle URL e il routing sulle view
 */
public class UrlMapperRouter {


    private static Logger logger = Logger.getLogger(UrlMapperRouter.class);

    private Map<Pattern, IAresAction> mPatternsMatch = new HashMap<>();

    private Map<Pattern, AresRouteEntry> mRouteConfigEntries = new HashMap<>();

    private Map<String, IAresAction> mActionStrings = new HashMap<>();



    public void addMap(String pattern, IAresAction action)
    {
        mPatternsMatch.put(Pattern.compile(pattern), action);
        buildActionString(action);
    }

    private void buildActionString(IAresAction aresAction)
    {
        AresAction actionAnn = aresAction.getClass().getAnnotation(AresAction.class);


        if (mActionStrings.values().stream().filter(s -> s.equals(aresAction)).count() == 0) {

            for (Method m : aresAction.getClass().getDeclaredMethods()) {
                if (m.isAnnotationPresent(MapRequest.class)) {

                    String buildAnnotation = String.format("%s.%s", actionAnn.name(), m.getName());


                    mActionStrings.put(buildAnnotation, aresAction);

                    logger.info(String.format("Building action strings => %s", buildAnnotation));

                }
            }
        }
    }

    public HashMap<String, ActionInfo> getActionMap()
    {

        HashMap<String, ActionInfo> resultHash = new HashMap<>();

        for (String action : mActionStrings.keySet()) {
            String methodName = action.split("\\.")[1];

            IAresAction aresAction = mActionStrings.get(action);

            for (Method m : aresAction.getClass().getDeclaredMethods()) {

                if (m.getName().equals(methodName)) {
                    if (m.isAnnotationPresent(MapRequest.class))
                    {
                        ActionInfo result = new ActionInfo();
                        result.setMethod(m);
                        result.setMapRequest(m.getAnnotation(MapRequest.class));
                        result.setAction(aresAction);
                        result.setActionAnnotation(aresAction.getClass().getAnnotation(AresAction.class));
                        result.setFullUrl(result.getActionAnnotation().baseUrl() + result.getMapRequest().path());

                        resultHash.put(action, result);
                    }
                }

            }
        }


            return resultHash;
    }

    public ActionInfo resolveActionString(String actionString)
    {

        ActionInfo result = new ActionInfo();


        if (!Strings.isNullOrEmpty(actionString)) {

            String methodName = actionString.split("\\.")[1];

            IAresAction action = mActionStrings.get(actionString);
            for(Method m : action.getClass().getDeclaredMethods())
            {
                if (m.getName().equals(methodName))
                {
                    result.setMethod(m);
                    result.setMapRequest(m.getAnnotation(MapRequest.class));
                    result.setAction(action);
                    result.setActionAnnotation(action.getClass().getAnnotation(AresAction.class));
                    result.setFullUrl(result.getActionAnnotation().baseUrl()  +result.getMapRequest().path());

                    return result;
                }
            }

        }

        return result;
    }

    public void addMap(String pattern, IAresAction action, AresRouteEntry entry)
    {
        mPatternsMatch.put(Pattern.compile(pattern), action);
        mRouteConfigEntries.put(Pattern.compile(pattern), entry);
    }


    public AresRouteEntry getConfigRouter(String uriPattern)
    {
        for (Pattern pattern: mRouteConfigEntries.keySet())
        {
            Matcher matcher = pattern.matcher(uriPattern);

            if (matcher.matches())
                return mRouteConfigEntries.get(pattern);
        }

        return null;
    }

    public IAresAction match(String uriPattern) {

        IAresAction action = null;

        boolean matches = false;


        for (Pattern pattern : mPatternsMatch.keySet()) {

            Matcher match = pattern.matcher(uriPattern);

            matches = match.matches();
            if (matches) {

                action = mPatternsMatch.get(pattern);

                break;
            }
        }

        return action;
    }

    public IAresAction getActionByName(String name)
    {

        for(IAresAction action : mPatternsMatch.values())
        {
            AresAction annotation = action.getClass().getAnnotation(AresAction.class);

            if (annotation.name().toLowerCase().equals(name.toLowerCase()))
                return action;
        }

        return null;
    }
}

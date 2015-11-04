package com.github.tgiachi.ares.engine.dispatcher;

import com.github.tgiachi.ares.annotations.actions.AresAction;
import com.github.tgiachi.ares.data.config.AresRouteEntry;
import com.github.tgiachi.ares.interfaces.actions.IAresAction;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Classe per il parse delle URL e il routing sulle view
 */
public class UrlMapperRouter {

    private Map<Pattern, IAresAction> mPatternsMatch = new HashMap<>();

    private Map<Pattern, AresRouteEntry> mRouteConfigEntries = new HashMap<>();



    public void addMap(String pattern, IAresAction action)
    {
        mPatternsMatch.put(Pattern.compile(pattern), action);
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

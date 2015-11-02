package com.github.tgiachi.ares.engine.dispatcher;

import com.github.tgiachi.ares.annotations.actions.MapRequest;
import com.github.tgiachi.ares.annotations.actions.RequestType;
import com.github.tgiachi.ares.interfaces.actions.IAresAction;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Classe per il parse delle URL e il routing sulle view
 */
public class UrlMapperRouter {

    private Map<Pattern, IAresAction> mPatternsMatch = new HashMap<>();

//    private Logger logger = Logger.getLogger(UrlMapperRouter.class);


    public void addMap(String pattern, IAresAction action)
    {
        mPatternsMatch.put(Pattern.compile(pattern), action);
    }

    public IAresAction match(String uriPattern) {
        IAresAction action = null;

        boolean matches = false;


        for (Pattern pattern : mPatternsMatch.keySet()) {

            Matcher match = pattern.matcher(uriPattern);

            matches = match.find();
            if (matches) {

                action = mPatternsMatch.get(pattern);

                break;
            }
        }

        return action;
    }
}

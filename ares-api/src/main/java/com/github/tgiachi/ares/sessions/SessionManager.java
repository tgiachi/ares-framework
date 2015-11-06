package com.github.tgiachi.ares.sessions;

import com.github.tgiachi.ares.annotations.actions.RequestType;
import com.github.tgiachi.ares.data.config.AresConfig;
import com.github.tgiachi.ares.data.config.AresDatabaseConfig;
import com.github.tgiachi.ares.data.config.AresRoutes;
import com.github.tgiachi.ares.data.debug.DebugSessionNavigationInfo;
import com.github.tgiachi.ares.data.debug.GenerationStat;
import com.github.tgiachi.ares.eventbus.ObservableVariablesManager;
import com.github.tgiachi.ares.interfaces.engine.IAresEngine;
import com.google.common.base.Strings;
import lombok.Getter;
import lombok.Setter;
import org.apache.log4j.Level;
import rx.functions.Action1;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Classe per salvare la sessione corrente dell'engine
 */
public class SessionManager {

    @Getter @Setter
    private static List<GenerationStat> generationStats = new ArrayList<>();

    @Getter @Setter
    private static IAresEngine engine;

    @Getter @Setter
    private static AresConfig config;

    @Getter @Setter
    private static String environment;

    @Getter @Setter
    private static DirectoriesConfig directoriesConfig = new DirectoriesConfig();

    @Getter @Setter
    private static AresDatabaseConfig databaseConfig;

    @Getter @Setter
    private static AresRoutes routes;


    public static void broadcastMessage(String key, Object value)
    {
        ObservableVariablesManager.updateVariable(key, value);
    }

    public static void subscribe(String name, Action1<? super Object> onNext)
    {
        ObservableVariablesManager.subscribe(name,onNext);
    }

    public static void debug(Object source, int resultCode, HttpServletRequest request,Level level, String action, RequestType type,  String text,  Object ... args)
    {
        DebugSessionNavigationInfo dSession = new DebugSessionNavigationInfo();
        dSession.setLevel(level);

        dSession.setRequest(request);
        if (!Strings.isNullOrEmpty(text))
        dSession.setLog(String.format(text, args));

        dSession.setSessionId(request.getSession().getId());
        dSession.setSource(source.getClass().getSimpleName());
        dSession.setNavigateUrl(action);
        dSession.setType(type);
        dSession.setResultCode(resultCode);

        broadcastMessage("DEBUG", dSession);

    }


}

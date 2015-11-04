package com.github.tgiachi.ares.sessions;

import com.github.tgiachi.ares.data.config.AresConfig;
import com.github.tgiachi.ares.data.config.AresDatabaseConfig;
import com.github.tgiachi.ares.data.config.AresRoutes;
import com.github.tgiachi.ares.data.debug.GenerationStat;
import com.github.tgiachi.ares.eventbus.ObservableVariablesManager;
import com.github.tgiachi.ares.interfaces.engine.IAresEngine;
import lombok.Getter;
import lombok.Setter;
import rx.functions.Action1;

import java.util.ArrayList;
import java.util.List;

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


}

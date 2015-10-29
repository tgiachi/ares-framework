package com.github.tgiachi.ares.sessions;

import com.github.tgiachi.ares.data.config.AresConfig;
import com.github.tgiachi.ares.interfaces.engine.IAresEngine;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * Classe per salvare la sessione corrente dell'engine
 */
public class SessionManager {

    @Getter @Setter
    private static IAresEngine engine;


    @Getter @Setter
    private static String rootDirectory;


    @Getter @Setter
    private static String appDirectory;

    @Getter @Setter
    private static String appConfigDirectory;


    @Getter @Setter
    private static AresConfig config;
}

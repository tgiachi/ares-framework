package com.github.tgiachi.ares.engine.utils;

/**
 * Classe per mantenere le constanti
 */
public class EngineConst {

    public static String ARES_BASEDIRECTORY = "ares.base.directory";

    public static String ARES_APP_NAME = "ares.application.name";

    public static String DEFAULT_CONFIG_DIRECTORY = "config";


    public static String getAresBasedirectory()
    {
        return System.getProperty(ARES_BASEDIRECTORY);
    }

    public static String getAresAppName()
    {
        return System.getProperty(ARES_APP_NAME);
    }
}

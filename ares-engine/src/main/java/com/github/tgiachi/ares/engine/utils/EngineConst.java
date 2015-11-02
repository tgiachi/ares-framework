package com.github.tgiachi.ares.engine.utils;

/**
 * Classe per mantenere le constanti
 */
public class EngineConst {

    public static String ARES_BASEDIRECTORY = "ares.base.directory";

    public static String ARES_APP_NAME = "ares.application.name";

    public static String DEFAULT_CONFIG_DIRECTORY = "config";
    public static String DEFAULT_TEMPLATE_DIRECTORY = "web";


    public static String MODEL_VAR_REQUEST_TYPE = "request_type";
    public static String MODEL_VAR_HEADERS = "headers";
    public static String MODEL_VAR_VALUES = "values";
    public static String MODEL_VAR_INVOKE_GENERATOR_TIME = "invoke_generation_time";
    public static String MODEL_VAR_GIT_PROPERTIES = "gitproperties";
    public static String MODEL_APP_NAME = "appname";
    public static String MODEL_APP_VERSION = "appversion";
    public static String MODEL_SESSION = "session";


    public static String getAresBasedirectory()
    {
        return System.getProperty(ARES_BASEDIRECTORY);
    }

    public static String getAresAppName()
    {
        return System.getProperty(ARES_APP_NAME);
    }
}

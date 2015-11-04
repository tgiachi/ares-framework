package com.github.tgiachi.ares.engine.utils;

import com.google.common.base.Strings;

/**
 * Classe per mantenere le constanti
 */
public class EngineConst {

    public static String ARES_BASEDIRECTORY = "ares.base.directory";

    public static String ARES_APP_NAME = "ares.application.name";

    public static String ARES_APP_ENVIRONMENT = "ares.application.env";

    public static String ARES_CREATE_DEFAULT_FILES = "ares.default.files";

    public static String DEFAULT_CONFIG_DIRECTORY = "config";
    public static String DEFAULT_CONFIG_FILENAME = "ares.config";


    public static String DEFAULT_VIEWS_DIRECTORY = "views";

    public static String DEFAULT_TEMPLATE_DIRECTORY = "web";
    public static String DEFAULT_DB_CONFIG_DIRECTORY = "db";
    public static String DEFAULT_DB_CONFIG_FILENAME = "database.json";
    public static String DEFAULT_ROUTES_CONFIG_DIRECTORY = "routes";
    public static String DEFAULT_ROUTES_CONFIG_FILENAME = "routes.json";




    public static String MODEL_VAR_ACTION = "action";
    public static String MODEL_VAR_REQUEST_TYPE = "request_type";
    public static String MODEL_VAR_HEADERS = "headers";
    public static String MODEL_VAR_VALUES = "values";
    public static String MODEL_VAR_INVOKE_GENERATOR_TIME = "invoke_generation_time";
    public static String MODEL_VAR_GIT_PROPERTIES = "gitproperties";
    public static String MODEL_APP_NAME = "appname";
    public static String MODEL_APP_VERSION = "appversion";
    public static String MODEL_SESSION = "session";
    public static String MODEL_SESSION_MAP = "session_map";
    public static String MODEL_ENVIRONMENT = "environment";

    public static String MODEL_CONTEXT_PATH = "context_path";

    public static String SESSION_USER_AUTHENTICATED = "is_authenticated";
    public static String SESSION_USER_DATA = "user_data";
    public static final String SESSION_PRE_AUTH = "pre_auth_url";
    public static final String SESSION_PREV_URL = "prev_url";



    public static String getAresBasedirectory()
    {
        return System.getProperty(ARES_BASEDIRECTORY);
    }

    public static String getAresAppName()
    {
        return System.getProperty(ARES_APP_NAME);
    }

    public static String getAresAppEnvironment()
    {
        String env = System.getProperty(ARES_APP_ENVIRONMENT);

        if (Strings.isNullOrEmpty(env))
            return "development";
        else
            return env;
    }

    public static boolean getAresCreateDefaultFiles()
    {
        String env = System.getProperty(ARES_CREATE_DEFAULT_FILES);

        return !Strings.isNullOrEmpty(env);

    }
}

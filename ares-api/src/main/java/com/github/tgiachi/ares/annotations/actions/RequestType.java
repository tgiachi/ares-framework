package com.github.tgiachi.ares.annotations.actions;

/**
 * Tipo di richiesta HTTP
 */
public enum RequestType {
    GET("GET"),
    POST("POST"),
    DELETE("DELETE"),
    PUT("PUT"),
    HEAD("HEAD"),
    TRACE("TRACE"),
    OPTIONS("OPTIONS");

    private final String text;

    RequestType(String text)
    {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}

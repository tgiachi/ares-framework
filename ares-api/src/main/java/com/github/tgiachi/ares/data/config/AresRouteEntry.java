package com.github.tgiachi.ares.data.config;

import com.github.tgiachi.ares.annotations.actions.RequestType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * Entry per le route
 */
@Data
@AllArgsConstructor
public class AresRouteEntry implements Serializable {

    private RequestType type;

    private String map;

    private String actionName;

    private String method;

    public AresRouteEntry()
    {

    }
}

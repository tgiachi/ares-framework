package com.github.tgiachi.ares.data.config;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * Entry della rotte statiche
 */
@Data
@AllArgsConstructor
public class AresStaticRouteEntry implements Serializable {

    private String urlMap;

    private String directory;

    private String processorClass;

    public AresStaticRouteEntry()
    {

    }


}

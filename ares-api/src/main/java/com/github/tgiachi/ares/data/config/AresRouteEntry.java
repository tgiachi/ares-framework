package com.github.tgiachi.ares.data.config;

import lombok.Data;

import java.io.Serializable;

/**
 * Entry della rotte statiche
 */
@Data
public class AresRouteEntry implements Serializable {

    private String urlMap;

    private String directory;

    private String processorClass;
}

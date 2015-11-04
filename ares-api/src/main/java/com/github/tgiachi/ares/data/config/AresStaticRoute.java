package com.github.tgiachi.ares.data.config;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe per salvare il routing delle static resources
 */
@Data
public class AresStaticRoute implements Serializable {

    private List<AresStaticRouteEntry> staticRoutes  = new ArrayList<>();
}

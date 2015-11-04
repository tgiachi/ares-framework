package com.github.tgiachi.ares.data.config;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * File di configurazione per le routes delle action
 */
@Data
public class AresRoutes implements Serializable {

    private List<AresRouteEntry> routes = new ArrayList<>();
}

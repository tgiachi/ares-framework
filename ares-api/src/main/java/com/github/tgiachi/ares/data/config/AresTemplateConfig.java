package com.github.tgiachi.ares.data.config;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Config per il caricamento dei template
 */
@Data
public class AresTemplateConfig implements Serializable {

    private List<String> templateDirectories = new ArrayList<>();

    private List<String> staticResourcesDirectories = new ArrayList<>();
}

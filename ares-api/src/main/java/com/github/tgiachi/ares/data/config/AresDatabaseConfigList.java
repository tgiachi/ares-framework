package com.github.tgiachi.ares.data.config;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Contenitore delle configurazioni database
 */
@Data
public class AresDatabaseConfigList implements Serializable {

    private List<AresDatabaseConfig> entries = new ArrayList<>();
}

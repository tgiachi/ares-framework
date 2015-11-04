package com.github.tgiachi.ares.data.config;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;

/**
 * File di configurazione per il database
 */
@Data
public class AresDatabaseConfig  implements Serializable {

    private String environment = "";

    private String driverClass = " ";

    private String url = " ";

    private String username = " ";

    private String password = " ";

    private HashMap<String, String> poolConfiguration = new HashMap<>();

}

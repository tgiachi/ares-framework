package com.github.tgiachi.ares.data.config;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Classe serializzabile per il salvaraggio delle informazioni di configurazione
 * E' stata data la classe AresConfig per cercare di astrarre il piu possibile
 * le configurazioni
 */
@Data
public class AresConfig implements Serializable
{
    private AresConfigHeader header = new AresConfigHeader();

    private HashMap<String, AresConfigEntry> entries = new HashMap<>();


    private AresDatabaseConfig databaseConfig = new AresDatabaseConfig();

    private AresTemplateConfig templateConfig = new AresTemplateConfig();

    private String databaseManager = "com.github.tgiachi.ares.database.DatabaseManager";

    private String filesystemManager = "com.github.tgiachi.ares.engine.filesystem.FileSystemManager";


    /**
     * Funzione per l'aggiunta di una entry nel config
     */
    public void addEntry(String key, Object value)
    {
        entries.put(key, new AresConfigEntry(value));

    }

    public <T> T getConfigEntry(String key)
    {
        return (T)entries.get(key).getObj();
    }


}

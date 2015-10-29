package com.github.tgiachi.ares.interfaces.engine;

import com.github.tgiachi.ares.interfaces.database.IDatabaseManager;
import com.github.tgiachi.ares.interfaces.fs.IFileSystemManager;

/**
 * Interfaccia per la creazione dell'Engine
 */
public interface IAresEngine {

    IDatabaseManager getDatabaseManager();

    IFileSystemManager getFileSystemManager();

    /**
     * Esegue la partenza dell'engine
     */
    void start();
}

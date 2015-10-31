package com.github.tgiachi.ares.interfaces.database;

import com.github.tgiachi.ares.data.db.AresQuery;
import com.github.tgiachi.ares.interfaces.managers.IAresManager;

/**
 * @author Tommaso Giachi
 *
 * Interfaccia per la creazione del database manager
 */
public interface IDatabaseManager extends IAresManager {

    AresQuery getNewQuery();

    void disposeQuery(AresQuery query);

    void shutdown();

}

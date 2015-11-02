package com.github.tgiachi.ares.interfaces.processors;

import com.github.tgiachi.ares.data.actions.ServletResult;

/**
 * Interfaccia dei processor
 */
public interface IAresProcessor {

    ServletResult parse(String action, String urlMap, String directory);
}

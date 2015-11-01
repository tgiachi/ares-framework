package com.github.tgiachi.ares.interfaces.container;

import com.github.tgiachi.ares.data.actions.ServletResult;

/**
 * Interfaccia per creare i mapper statici
 */
public interface IStaticMapper {

    ServletResult getStaticResource(String request);
}

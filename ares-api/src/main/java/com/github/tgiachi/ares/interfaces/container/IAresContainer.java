package com.github.tgiachi.ares.interfaces.container;

import com.github.tgiachi.ares.interfaces.engine.IAresEngine;

import java.util.List;

/**
 * Interfaccia per creare il conteiner di bean
 */
public interface IAresContainer {

    void init(IAresEngine engine);

    Object resolveWires(Object obj);

    <T> List<T> getBeansImplementInterface(Class<?> classz);
}

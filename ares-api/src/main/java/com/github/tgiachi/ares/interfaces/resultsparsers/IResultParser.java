package com.github.tgiachi.ares.interfaces.resultsparsers;

import com.github.tgiachi.ares.data.actions.ServletResult;
import com.github.tgiachi.ares.data.template.DataModel;
import com.github.tgiachi.ares.interfaces.engine.IAresEngine;

import java.lang.reflect.Method;

/**
 * Interfaccia per creare i result parsers
 */
public interface IResultParser {

    void init(IAresEngine engine);

    ServletResult parse( DataModel model,Method method, Object invoker, Object[] params) throws Exception;

}

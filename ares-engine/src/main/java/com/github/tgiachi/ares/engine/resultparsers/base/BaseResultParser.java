package com.github.tgiachi.ares.engine.resultparsers.base;

import com.github.tgiachi.ares.data.actions.ServletResult;
import com.github.tgiachi.ares.data.template.DataModel;
import com.github.tgiachi.ares.interfaces.engine.IAresEngine;
import com.github.tgiachi.ares.interfaces.resultsparsers.IResultParser;
import lombok.AccessLevel;
import lombok.Getter;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * Classe base per i result parser
 */
public class BaseResultParser implements IResultParser {

    private Logger logger;

    @Getter(AccessLevel.PROTECTED)
    private IAresEngine engine;

    @Override
    public void init(IAresEngine engine) {
        this.engine = engine;
        this.logger = Logger.getLogger(getClass());
    }

    @Override
    public ServletResult parse(DataModel model, Method method, Object invoker, Object[] params) throws Exception {
        return null;
    }

    protected void log(Level level, String text, Object ... args)
    {
        logger.log(level, String.format(text, args));
    }
}

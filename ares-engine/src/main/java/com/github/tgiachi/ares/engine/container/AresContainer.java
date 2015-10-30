package com.github.tgiachi.ares.engine.container;

import com.github.tgiachi.ares.annotations.container.AresBean;
import com.github.tgiachi.ares.engine.reflections.ReflectionUtils;
import com.github.tgiachi.ares.interfaces.container.IAresContainer;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Container default per i beans
 */
public class AresContainer implements IAresContainer{

    private Logger logger = Logger.getLogger(AresContainer.class);

    private List<Class<?>> mAvaiableBeans = new ArrayList<>();

    @Override
    public void scanForBeans() {

        try
        {
            Set<Class<?>> classes =  ReflectionUtils.getAnnotation(AresBean.class);

            log(Level.INFO, "Found %s beans", classes.size());
        }
        catch (Exception ex)
        {
            log(Level.FATAL, "Error during initializing ares bean container => %s ",ex.getMessage());
        }

    }

    protected void log(Level level, String text, Object ... args)
    {
        logger.log(level, String.format(text, args));
    }
}

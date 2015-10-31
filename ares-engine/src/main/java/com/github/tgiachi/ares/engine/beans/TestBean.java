package com.github.tgiachi.ares.engine.beans;

import com.github.tgiachi.ares.annotations.container.AresBean;
import com.github.tgiachi.ares.annotations.container.AresInject;
import com.github.tgiachi.ares.annotations.container.AresPostConstruct;
import com.github.tgiachi.ares.interfaces.container.IAresBean;
import com.github.tgiachi.ares.interfaces.engine.IAresEngine;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * Test bean
 */

@AresBean
public class TestBean implements IAresBean {

    @AresInject
    private IAresEngine engine;

    @AresInject
    private Logger logger;

    @Override
    public void ready() {

        logger.log(Level.INFO, "Oh Oh Oh! Ares Inject works!");

    }

    @AresPostConstruct
    private void postConstructTest()
    {
        logger.log(Level.INFO, "Post construct!");
    }
}

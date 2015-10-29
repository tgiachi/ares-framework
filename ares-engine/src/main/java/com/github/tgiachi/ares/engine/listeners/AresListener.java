package com.github.tgiachi.ares.engine.listeners;

import com.github.tgiachi.ares.engine.engine.AresEngine;
import com.github.tgiachi.ares.sessions.SessionManager;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Listener del framework,
 * Viene caricato all'avvio di tomcat
 */
@WebListener
public class AresListener implements ServletContextListener {

    private static Logger logger = Logger.getLogger(AresListener.class);


    @Override
    public void contextInitialized(ServletContextEvent sce) {

        System.out.println("ServletContextListener started");
        log(Level.INFO, "Inizializzazione Engine");

        SessionManager.setEngine(new AresEngine());

        SessionManager.getEngine().start();


    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }

    protected void log(Level level, String text, String ... args)
    {
        logger.log(level, String.format(text, args));
    }
}

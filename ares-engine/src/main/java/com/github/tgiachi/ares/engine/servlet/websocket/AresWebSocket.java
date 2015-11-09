package com.github.tgiachi.ares.engine.servlet.websocket;

import com.github.tgiachi.ares.data.websocket.AresWBMessage;

import com.github.tgiachi.ares.engine.servlet.websocket.encoders.AresMessageDecoder;
import com.github.tgiachi.ares.engine.servlet.websocket.encoders.AresMessageEncoder;
import com.github.tgiachi.ares.sessions.SessionManager;
import lombok.AccessLevel;
import lombok.Getter;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.*;


/**
 * Classe base per creare i websockets
 */

@ServerEndpoint(value = "/ws", decoders = {AresMessageDecoder.class}, encoders = {AresMessageEncoder.class})
public class AresWebSocket  {

    @Getter(AccessLevel.PROTECTED)
    private String queueName;


   // private static final Set< Session > sessions = Collections.synchronizedSet(new HashSet<Session>());
    private static final Map<String, Session> sessions = Collections.synchronizedMap(new HashMap<>());

    private static Logger logger;

    public AresWebSocket()
    {
        logger =  Logger.getLogger(getClass());

        log(Level.INFO, "Web socket up");
    }

    protected void initQueue()
    {
        String inputQueue = "IN_MESSAGES_"+queueName.toUpperCase();
        String outputQueue = "OUT_MESSAGES_"+queueName.toUpperCase();

        SessionManager.subscribe(inputQueue, o -> {});
        SessionManager.subscribe(outputQueue,o -> {});

        log(Level.INFO, "Subscribing %s and %s", inputQueue, outputQueue);

    }

    private void processQueueMessage(AresWBMessage message)
    {

    }


    @OnOpen
    public void onOpen(final Session session)
    {
        log(Level.INFO, "New session opened %s", session.getId());
        sessions.put(session.getId(), session);


    }

    @OnClose
    public void onClose(final  Session session)
    {
        log(Level.INFO, "Session closed %s", session.getId());
        sessions.remove(session);
    }

    @OnMessage
    public void OnMessage(final AresWBMessage message, final Session session)
    {
        try
        {
            broadcast(message);

        }
        catch (Exception ex)
        {
            log(Level.FATAL, "Error receiving message from session %s => %s", session.getId(), ex.getMessage());
        }
    }

    public void broadcast(AresWBMessage message)
    {


            sessions.forEach((s, session) -> {
                try
                {
                    sendMessage(session, message);
                }
                catch (Exception ex)
                {

                }
            });

    }



    private void sendMessage(Session session, AresWBMessage message) throws Exception
    {
        session.getBasicRemote().sendObject(message);
    }



    protected void log(Level level, String text, Object ... args)
    {
        logger.log(level, String.format(text, args));
    }




}

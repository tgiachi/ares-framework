package com.github.tgiachi.ares.engine.servlet.websocket;

import com.github.tgiachi.ares.data.websocket.AresWBMessage;

import com.github.tgiachi.ares.engine.servlet.websocket.encoders.AresMessageDecoder;
import com.github.tgiachi.ares.engine.servlet.websocket.encoders.AresMessageEncoder;
import com.github.tgiachi.ares.sessions.SessionManager;
import com.google.common.base.Strings;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.*;


/**
 * Classe base per creare i websockets
 */


public class AresWebSocket  {

    @Getter(AccessLevel.PROTECTED)
    @Setter(AccessLevel.PROTECTED)
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
        SessionManager.subscribe(outputQueue,this::processQueueMessage);

        log(Level.INFO, "Subscribing %s and %s", inputQueue, outputQueue);

    }

    private void processQueueMessage(Object message)
    {
        AresWBMessage aresWBMessage = (AresWBMessage)message;

        log(Level.DEBUG, "Process message uid %s for source %s", aresWBMessage.getUid(), aresWBMessage.getSource());

        if (Strings.isNullOrEmpty(aresWBMessage.getDestination()))
        {
            broadcast(aresWBMessage);
        }
        else
        {
            try
            {
                sendMessage(getSessionById(aresWBMessage.getDestination()), aresWBMessage);
            }
            catch (Exception ex)
            {
                log(Level.FATAL, "Error during send message => %s", ex.getMessage());
            }

        }

    }

    private Session getSessionById(String uuid)
    {
        if (sessions.get(uuid) != null)
            return sessions.get(uuid);

        return null;
    }

    @OnError
    public void onError(Session session, Throwable throwable)
    {
        log(Level.FATAL, "Error in session %s => %s", session.getId(), throwable.getMessage());

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
            processOnMessage(message, session);

        }
        catch (Exception ex)
        {
            log(Level.FATAL, "Error receiving message from session %s => %s", session.getId(), ex.getMessage());
        }
    }

    protected void processOnMessage(final AresWBMessage message, final Session session)
    {

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

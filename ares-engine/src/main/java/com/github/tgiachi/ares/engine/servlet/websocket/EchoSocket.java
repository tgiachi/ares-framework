package com.github.tgiachi.ares.engine.servlet.websocket;

import com.github.tgiachi.ares.data.websocket.AresWBMessage;
import com.github.tgiachi.ares.engine.servlet.websocket.encoders.AresMessageDecoder;
import com.github.tgiachi.ares.engine.servlet.websocket.encoders.AresMessageEncoder;

import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 * Socket di esempio
 */
@ServerEndpoint(value = "/ws", decoders = {AresMessageDecoder.class}, encoders = {AresMessageEncoder.class})
public class EchoSocket extends AresWebSocket {

    public EchoSocket() {
        super();
        setQueueName("ECHO");
        initQueue();

    }

    @Override
    protected void processOnMessage(AresWBMessage message, Session session) {
        broadcast(message);
    }
}

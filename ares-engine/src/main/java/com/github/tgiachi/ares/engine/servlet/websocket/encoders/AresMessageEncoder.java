package com.github.tgiachi.ares.engine.servlet.websocket.encoders;

import com.github.tgiachi.ares.data.websocket.AresWBMessage;
import com.github.tgiachi.ares.engine.serializer.JsonSerializer;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

/**
 * Encoder per i messaggi WebSocket
 */
public class AresMessageEncoder implements Encoder.Text<AresWBMessage> {

    private static Logger logger = Logger.getLogger(AresMessageEncoder.class);

    @Override
    public String encode(AresWBMessage object) throws EncodeException {
        return JsonSerializer.Serialize(object);
    }

    @Override
    public void init(EndpointConfig config) {

    }

    @Override
    public void destroy() {

    }

    protected void log(Level level, String text, Object ... args)
    {
        logger.log(level, String.format(text, args));
    }
}

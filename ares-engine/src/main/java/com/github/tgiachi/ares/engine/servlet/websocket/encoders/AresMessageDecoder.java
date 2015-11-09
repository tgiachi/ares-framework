package com.github.tgiachi.ares.engine.servlet.websocket.encoders;

import com.github.tgiachi.ares.data.websocket.AresWBMessage;
import com.github.tgiachi.ares.engine.serializer.JsonSerializer;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

/**
 * Decoder per i messaggi webSocket
 */
public class AresMessageDecoder implements Decoder.Text<AresWBMessage> {
    @Override
    public AresWBMessage decode(String s) throws DecodeException
    {
        return JsonSerializer.Deserialize(s, AresWBMessage.class);
    }

    @Override
    public boolean willDecode(String s)
    {
        return true;
    }

    @Override
    public void init(EndpointConfig config)
    {

    }

    @Override
    public void destroy()
    {

    }
}

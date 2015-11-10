package com.github.tgiachi.ares.data.websocket;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.UUID;

/**
 * Messaggio serializzabile per spedire al websocket
 */
@Data
public class AresWBMessage implements Serializable {

    private String uid;

    private int messageId = 0;

    private String source;

    private String destination;


    private HashMap<String, Object> data = new HashMap<>();


    public AresWBMessage addData(String key, Object value)
    {
        data.put(key, value);

        return this;
    }

    public AresWBMessage setMessageId(int messageId)
    {
        this.messageId = messageId;
        return this;
    }

    public static AresWBMessage newMessage(int messageId)
    {
        AresWBMessage message = new AresWBMessage();
        message.setUid(UUID.randomUUID().toString());
        return message.setMessageId(messageId);

    }

    public  AresWBMessage setSource(String source)
    {
        this.source = source;

        return this;
    }

    public AresWBMessage setDestination(String destination)
    {
        this.destination =destination;
        return this;
    }
}

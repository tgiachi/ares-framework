package com.github.tgiachi.ares.data.actions;

import lombok.Data;

import java.io.Serializable;

/**
 * Il risultato che andra' a stampante sullo stream
 */
@Data
public class ServletResult implements Serializable {

    private byte[] result;

    private String mimeType = "text/html";

    private int returnCode = 200;

    private Exception exception;

    public ServletResult(byte[] result)
    {
        this.result = result;
    }

    public ServletResult()
    {

    }

    public ServletResult(String mimeType, byte[] result )
    {
        this.mimeType = mimeType;
        this.result = result;
    }

    public ServletResult(int returnCode)
    {
        this.returnCode = returnCode;
    }
}

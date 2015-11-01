package com.github.tgiachi.ares.data.actions;

import com.github.tgiachi.ares.data.template.TemplateResult;
import lombok.Data;

import java.io.Serializable;

/**
 * Il risultato che andra' a stampante sullo stream
 */
@Data
public class ServletResult implements Serializable {

    private byte[] result;

    private String mimeType = "text/html";

    private int errorCode = -1;

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
}

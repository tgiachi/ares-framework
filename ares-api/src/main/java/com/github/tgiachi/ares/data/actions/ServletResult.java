package com.github.tgiachi.ares.data.actions;

import lombok.Data;

import javax.servlet.http.Cookie;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    private List<Cookie> cookies = new ArrayList<>();

    private HashMap<String, String> headers = new HashMap<>();

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

package com.github.tgiachi.ares.data.actions;

import com.github.tgiachi.ares.data.template.TemplateResult;
import lombok.Data;

import java.io.Serializable;

/**
 * Il risultato che andra' a stampante sullo stream
 */
@Data
public class ServletResult implements Serializable {

    private TemplateResult result;

    private String mimeType = "text/html";

    public ServletResult(TemplateResult result)
    {
        this.result = result;
    }

    public ServletResult()
    {

    }
}

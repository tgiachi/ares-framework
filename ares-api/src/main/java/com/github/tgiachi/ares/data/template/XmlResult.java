package com.github.tgiachi.ares.data.template;

import lombok.Data;

import java.io.Serializable;

/**
 * Restituzione dell'XML
 */
@Data
public class XmlResult implements Serializable {

    private Serializable data;

    public XmlResult(Serializable data)
    {
        this.data = data;
    }

    public XmlResult()
    {

    }
}

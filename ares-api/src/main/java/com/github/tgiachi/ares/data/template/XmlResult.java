package com.github.tgiachi.ares.data.template;

import com.github.tgiachi.ares.data.template.base.BaseResult;
import lombok.Data;

import java.io.Serializable;

/**
 * Restituzione dell'XML
 */
@Data
public class XmlResult extends BaseResult {

    private Object data;

    public XmlResult(Object data)
    {
        this.data = data;
    }

    public XmlResult()
    {

    }
}

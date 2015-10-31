package com.github.tgiachi.ares.data.template;

import lombok.Data;

import java.io.Serializable;

/**
 * Generazione dello YAML
 */
@Data
public class YamlResult implements Serializable {

    private Serializable data;

    public YamlResult(Serializable data)
    {
        this.data = data;
    }

    public YamlResult()
    {

    }
}

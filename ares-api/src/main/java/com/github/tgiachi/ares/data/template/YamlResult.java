package com.github.tgiachi.ares.data.template;

import com.github.tgiachi.ares.data.template.base.BaseResult;
import lombok.Data;

import java.io.Serializable;

/**
 * Generazione dello YAML
 */
@Data
public class YamlResult extends BaseResult {

    private Serializable data;

    public YamlResult(Serializable data)
    {
        this.data = data;
    }

    public YamlResult()
    {

    }
}

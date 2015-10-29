package com.github.tgiachi.ares.data.config;

import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

/**
 * Entry della configurazione
 */
@Data
public class AresConfigEntry implements Serializable {
    private String classz;
    private Object obj;


    public AresConfigEntry()
    {

    }

    public AresConfigEntry(Object value)
    {
        this.classz = value.getClass().getName();
        this.obj = value;
    }
}

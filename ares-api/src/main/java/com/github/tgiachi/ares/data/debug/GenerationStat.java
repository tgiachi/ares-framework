package com.github.tgiachi.ares.data.debug;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Oggetto per tracciare la velocita di esecuzione dei componenti
 */
@Data
public class GenerationStat implements Serializable {

    private String className;

    public String operation;

    private long generationTime;

    private Date date;

    private String log;

    public GenerationStat()
    {

    }

    public GenerationStat(Class<?> classz, String operation, long generationTime, String log)
    {
        this.className = classz.getName();
        this.operation = operation;
        this.generationTime = generationTime;
        this.date = new Date();
        this.log = log;
    }
}

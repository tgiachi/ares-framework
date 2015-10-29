package com.github.tgiachi.ares.data.config;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

/**
 * Intestazioni della configurazione
 */
@Data
public class AresConfigHeader implements Serializable {

    private String author;

    private Date creationDate;

    private Date lastModifiedDate;

    private String description;


    public AresConfigHeader()
    {
        creationDate = new Date();
        lastModifiedDate = new Date();
    }

}

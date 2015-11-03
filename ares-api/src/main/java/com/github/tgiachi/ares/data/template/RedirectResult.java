package com.github.tgiachi.ares.data.template;

import lombok.Data;

import java.io.Serializable;

/**
 * Esegue il redirect ad una pagina
 */
@Data
public class RedirectResult implements Serializable
{
    private String location;


    public RedirectResult()
    {

    }

    public RedirectResult(String location)
    {
        this.location = location;
    }
}

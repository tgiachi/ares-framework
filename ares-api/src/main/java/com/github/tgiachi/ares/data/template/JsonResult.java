package com.github.tgiachi.ares.data.template;

import lombok.Data;

import java.io.Serializable;

/**
 * Classe per restituire Json
 */
@Data
public class JsonResult implements Serializable {


    private Serializable data;

    public JsonResult(Serializable data)
    {
        this.data = data;
    }

    public JsonResult()
    {

    }
}

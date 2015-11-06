package com.github.tgiachi.ares.data.template;

import com.github.tgiachi.ares.data.template.base.BaseResult;
import lombok.Data;

import java.io.Serializable;

/**
 * Classe per restituire Json
 */
@Data
public class JsonResult extends BaseResult {


    private Object data;

    public JsonResult(Object data)
    {
        this.data = data;
    }

    public JsonResult()
    {

    }
}

package com.github.tgiachi.ares.data.template;

import com.github.tgiachi.ares.data.template.base.BaseResult;
import lombok.Data;

import java.io.Serializable;

/**
 * Esegue il redirect ad una pagina
 */
@Data
public class RedirectResult extends BaseResult

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

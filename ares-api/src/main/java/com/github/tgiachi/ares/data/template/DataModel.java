package com.github.tgiachi.ares.data.template;

import com.github.tgiachi.ares.data.template.base.BaseResult;
import lombok.Getter;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Classe per passare il data model ai template
 */
public class DataModel extends BaseResult {

    @Getter
    private HashMap<String, Object> dataMap = new HashMap<>();

    public void addAttribute(String key, Object value)
    {
        dataMap.put(key, value);
    }

    public <T> T getAttribute(String key)
    {
        return (T)dataMap.get(key);
    }


}

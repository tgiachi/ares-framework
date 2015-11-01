package com.github.tgiachi.ares.engine.dispacher;

import com.github.tgiachi.ares.annotations.container.AresStaticResources;
import com.github.tgiachi.ares.data.actions.ServletResult;
import com.github.tgiachi.ares.interfaces.container.IStaticMapper;

/**
 * Static mapper default
 */
@AresStaticResources(directory = "/img/", mapTo = "/img")
public class DefaultStaticMapper implements IStaticMapper{

    public DefaultStaticMapper()
    {

    }

    @Override
    public ServletResult getStaticResource(String request) {
        ServletResult result = new ServletResult();

        try
        {

        }
        catch (Exception ex)
        {

        }

        return result;

    }
}

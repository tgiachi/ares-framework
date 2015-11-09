package com.github.tgiachi.ares.chain;

import com.github.tgiachi.ares.annotations.actions.HeaderProcessor;
import com.github.tgiachi.ares.data.actions.ServletResult;
import com.github.tgiachi.ares.interfaces.chain.IChainProcessor;

/**
 * Created by squid on 11/6/15.
 */
@HeaderProcessor
public class TestHeaderProcessor implements IChainProcessor {
    @Override
    public <T> T beforeResult(T obj) throws Exception {
        return obj;
    }

    @Override
    public <T> T afterResult(T obj) throws Exception {
       if (obj instanceof ServletResult)
       {
           return (T) ((ServletResult) obj).getHeaders().put("X-Powered-by", "Ares framework");
       }

        return obj;
    }
}

package com.github.tgiachi.ares.chain.manager.base;

import com.github.tgiachi.ares.interfaces.chain.IChainProcessor;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Manager per processare le catene
 */
public class BaseChainProcessor {

    private static Logger logger = Logger.getLogger(BaseChainProcessor.class);

    private List<IChainProcessor> processorList = new ArrayList<>();

    public <T> T executeBeforeChainProcessor(T obj)
    {
        for (IChainProcessor processor : processorList)
        {
            try
            {
                obj = processor.beforeResult(obj);
            }
            catch (Exception ex)
            {
                log(Level.FATAL, "Error during call processor %s => %s", processor.getClass().getName(), ex.getMessage());
            }

        }

        return obj;

    }

    public <T> T executeAfterChainProcessor(T obj)
    {
        for (IChainProcessor processor : processorList)
        {
            try
            {
                obj = processor.afterResult(obj);
            }
            catch (Exception ex)
            {
                log(Level.FATAL, "Error during call processor %s => %s", processor.getClass().getName(), ex.getMessage());
            }

        }

        return obj;

    }

    public void addToChain(IChainProcessor obj)
    {
        processorList.add(obj);
    }

    protected void log(Level level, String text, Object ... args)
    {
        logger.log(level, String.format(text, args));
    }


}

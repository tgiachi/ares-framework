package com.github.tgiachi.ares.interfaces.chain;

/**
 * Intefaccia per creare i chain processor
 */
public interface IChainProcessor {

    <T> T beforeResult(T obj) throws Exception;

    <T> T afterResult(T obj) throws Exception;

}

package com.github.tgiachi.ares.engine.serializer;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.Dom4JDriver;

import java.lang.reflect.Type;

/**
 * Implementazione della serializzazione XML
 */
public class XmlSerializer {

    private static XStream xStream = new XStream(new Dom4JDriver());

    public static String toXml(Object data)
    {
        return xStream.toXML(data);
    }

    public <T> T fromXml(String xml, Type type)
    {
        return (T)xStream.fromXML(xml, type);
    }
}

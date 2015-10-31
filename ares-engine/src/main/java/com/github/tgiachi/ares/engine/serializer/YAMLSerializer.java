package com.github.tgiachi.ares.engine.serializer;

import org.yaml.snakeyaml.Yaml;

import java.lang.reflect.Type;

/**
 * Serializzatore YAML
 */
public class YAMLSerializer {

    private static Yaml mYaml = new Yaml();

    public static String toYaml(Object data)
    {
        return mYaml.dump(data);
    }

    public static <T> T fromYaml(String yaml, Type type)
    {
        return (T)mYaml.load(yaml);
    }
}

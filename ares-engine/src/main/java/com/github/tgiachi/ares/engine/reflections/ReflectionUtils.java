package com.github.tgiachi.ares.engine.reflections;



import org.reflections.Reflections;


import java.util.Set;

/**
 * Utility class for get reflection
 */
public class ReflectionUtils {

    public static Set<Class<?>> getAnnotation(Class type) {
        return new Reflections("com").getTypesAnnotatedWith(type);
    }

    public static Set<Class<?>> getAnnotation(Class type, boolean scanJcl)
    {
        if (!scanJcl)
            return getAnnotation(type);
        //else
        //    return new Reflections(JclContext.get(), "com").getTypesAnnotatedWith(type);
        return null;
    }
}

package com.github.tgiachi.ares.engine.container;

import com.github.tgiachi.ares.annotations.container.AresBean;
import com.github.tgiachi.ares.annotations.container.AresInject;
import com.github.tgiachi.ares.annotations.container.AresPostConstruct;
import com.github.tgiachi.ares.engine.reflections.ReflectionUtils;
import com.github.tgiachi.ares.interfaces.container.AresContainerType;
import com.github.tgiachi.ares.interfaces.container.IAresBean;
import com.github.tgiachi.ares.interfaces.container.IAresContainer;
import com.github.tgiachi.ares.interfaces.database.IDatabaseManager;
import com.github.tgiachi.ares.interfaces.engine.IAresEngine;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Container default per i beans
 */
public class AresContainer implements IAresContainer {

    private Logger logger = Logger.getLogger(AresContainer.class);

    private List<Class<?>> mAvaiableBeans = new ArrayList<>();

    private List<Object> mSingletonBeans = new ArrayList<>();

    private IAresEngine engine;

    @Override
    public void init(IAresEngine engine) {
        this.engine = engine;
        scanForBeans();
        autoWireBeans();
        scanForPostConstruct();
    }

    private void scanForPostConstruct() {
        for (Object obj : mSingletonBeans)
        {
            try
            {
                for(Method m : obj.getClass().getDeclaredMethods())
                {
                    if (m.isAnnotationPresent(AresPostConstruct.class))
                    {
                        m.setAccessible(true);
                        m.invoke(obj);
                    }
                }

            }
            catch (Exception ex)
            {
                log(Level.INFO, "Error during call postConstrcut! => %s", ex.getMessage());
            }
        }
    }


    public void scanForBeans() {

        try {
            Set<Class<?>> classes = ReflectionUtils.getAnnotation(AresBean.class);

            log(Level.INFO, "Found %s beans", classes.size());

            for (Class<?> classz : classes) {
                log(Level.INFO, "Loading class %s", classz.getSimpleName());

                AresBean annotation = classz.getAnnotation(AresBean.class);

                log(Level.INFO, "Bean %s is %s", classz.getSimpleName(), annotation.type());

                if (annotation.type() == AresContainerType.SINGLETON) {
                    try {
                        Object aresBean = classz.newInstance();

                        mSingletonBeans.add(aresBean);

                    } catch (Exception ex) {
                        log(Level.INFO, "Error during initializing %s bean ==> %s", classz.getSimpleName(), ex.getMessage());
                    }
                }
            }
        } catch (Exception ex) {
            log(Level.FATAL, "Error during initializing ares bean container => %s ", ex.getMessage());
        }

    }

    private void autoWireBeans() {
        for (Object obj : mSingletonBeans) {
            resolveWires(obj);
        }
    }

    private boolean implementInterface(Class<?> source, Class<?> interfaceClass) {
        for (Class<?> inf : source.getInterfaces()) {
            if (inf.equals(interfaceClass))
                return true;
        }

        return false;
    }

    @Override
    public Object resolveWires(Object obj) {
        try {
            List<Field> fields = new ArrayList<>();
            fields.addAll(Arrays.asList((Field[]) obj.getClass().getDeclaredFields()));

           if (!obj.getClass().getSuperclass().equals(Object.class))
            {
                fields.addAll(Arrays.asList((Field[]) obj.getClass().getSuperclass().getDeclaredFields()));
            }

            for (Field field : fields) {
                // aresInject.
                if (field.isAnnotationPresent(AresInject.class)) {
                    log(Level.INFO, "%s.%s is annotated with AresInject", obj.getClass().getSimpleName(), field.getName());

                    Object toSet = null;

                    if (field.getType().equals(IAresEngine.class))
                        toSet = engine;

                    if (field.getType().equals(IAresContainer.class))
                        toSet = this;

                    if (field.getType().equals(IDatabaseManager.class))
                        toSet = engine.getDatabaseManager();

                    if (field.getType().equals(Logger.class))
                        toSet = Logger.getLogger(obj.getClass());

                    if (toSet == null) {
                        Optional<Object> bean = mSingletonBeans.stream().filter(s -> s.getClass().equals(field.getType())).findFirst();

                        if (bean.isPresent())
                            toSet = bean.get();
                    }

                    if (toSet == null) {
                        Optional<Class<?>> bean = mAvaiableBeans.stream().filter(s -> s.equals(field.getType())).findFirst();

                        try {
                            if (bean.isPresent())
                                toSet = bean.get().newInstance();
                        } catch (Exception ex) {
                            log(Level.FATAL, "Error during autowire type %s => %s", field.getType(), ex.getMessage());
                        }
                    }

                    field.setAccessible(true);
                    try
                    {
                        field.set(obj, toSet);
                    } catch (Exception ex) {
                        log(Level.FATAL, "Error during set field type %s => %s", field.getType(), ex.getMessage());
                    }
                }
            }

            if (implementInterface(obj.getClass(), IAresBean.class)) {
                ((IAresBean) obj).ready();
            }
        } catch (Exception ex) {
            log(Level.FATAL, "Error during resolve wires for obj %s ==> %s", obj.getClass(), ex.getMessage());
        }

        return obj;
    }

    protected void log(Level level, String text, Object ... args)
    {
        logger.log(level, String.format(text, args));
    }
}

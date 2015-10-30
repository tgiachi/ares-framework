package com.github.tgiachi.ares.annotations.container;

import com.github.tgiachi.ares.interfaces.container.AresContainerType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotazione per creare i bean
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface AresBean {

    AresContainerType type() default AresContainerType.SINGLETON;

}

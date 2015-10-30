package com.github.tgiachi.ares.annotations.actions;

/**
 * Il tipo di risultato che deve produrre l'action
 */

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ContentResult {
    String type() default "text/html";
}

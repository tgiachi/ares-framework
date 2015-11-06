package com.github.tgiachi.ares.annotations.actions;

import com.github.tgiachi.ares.interfaces.actions.IAresAction;
import lombok.Data;

import java.lang.reflect.Method;

/**
 * Classe per restituire l'action e il metodo
 */
@Data
public class ActionInfo {

    private String fullUrl = "";

    private IAresAction action;

    private Method method;

    private MapRequest mapRequest;

    private AresAction actionAnnotation;
}

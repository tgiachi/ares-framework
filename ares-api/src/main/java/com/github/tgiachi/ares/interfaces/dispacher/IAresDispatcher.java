package com.github.tgiachi.ares.interfaces.dispacher;

import com.github.tgiachi.ares.annotations.actions.ActionInfo;
import com.github.tgiachi.ares.annotations.actions.RequestType;
import com.github.tgiachi.ares.data.actions.ServletResult;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

/**
 * Sistema per il routing delle view, static, ecc...
 */
public interface IAresDispatcher {

    ServletResult dispatch(String action, RequestType type, HashMap<String, String> headers, HashMap<String, String> values, HttpServletRequest request);

    String getAction(String action );

    HashMap<String, ActionInfo> getActionsMap();
}

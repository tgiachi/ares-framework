package com.github.tgiachi.ares.engine.actions;

import com.github.tgiachi.ares.annotations.actions.AresAction;
import com.github.tgiachi.ares.annotations.actions.GetParam;
import com.github.tgiachi.ares.annotations.actions.MapRequest;
import com.github.tgiachi.ares.annotations.container.AresBean;
import com.github.tgiachi.ares.annotations.container.AresInject;
import com.github.tgiachi.ares.annotations.container.AresPostConstruct;
import com.github.tgiachi.ares.data.actions.AresViewBag;
import com.github.tgiachi.ares.data.debug.DebugSessionInfo;
import com.github.tgiachi.ares.data.debug.DebugSessionNavigationInfo;
import com.github.tgiachi.ares.data.template.DataModel;
import com.github.tgiachi.ares.interfaces.actions.IAresAction;
import com.github.tgiachi.ares.sessions.SessionManager;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Action per visualizzare il debug
 */
@AresAction(baseUrl = "/debug", name = "debug")
@AresBean
public class DebugAction implements IAresAction {


    private static HashMap<DebugSessionInfo, List<DebugSessionNavigationInfo>> mDebugActions = new HashMap<>();

    @AresInject
    private Logger logger;

    @AresPostConstruct
    public void init()
    {
        log(Level.INFO, "Subscribing event queue");
        SessionManager.subscribe("DEBUG", this::onSessionAction);


    }

    private void log(Level level, String text, Object ... args)
    {
        logger.log(level, String.format(text, args));
    }

    private void onSessionAction(Object o)
    {
        DebugSessionNavigationInfo sessionInfo = (DebugSessionNavigationInfo)o;
        DebugSessionInfo debugSessionInfo = null;

        if (mDebugActions.keySet().stream().filter(s -> s.getSessionId().equals(sessionInfo.getSessionId())).findFirst().isPresent())
            debugSessionInfo = mDebugActions.keySet().stream().filter(s -> s.getSessionId().equals(sessionInfo.getSessionId())).findFirst().get();

        if (debugSessionInfo == null)
        {
            debugSessionInfo = new DebugSessionInfo();
            debugSessionInfo.setSessionId(sessionInfo.getSessionId());
            debugSessionInfo.setRemoteIp(sessionInfo.getRequest().getRemoteAddr());

            mDebugActions.put(debugSessionInfo, new ArrayList<>());
        }

        mDebugActions.get(debugSessionInfo).add(sessionInfo);

    }

    @MapRequest(path = "/sessionlist.html")
    public AresViewBag doDebugSessionList(DataModel model)
    {
        model.addAttribute("session_list", mDebugActions.keySet());

        return new AresViewBag(model);

    }

    @MapRequest(path = "/session_view.html")
    public AresViewBag doDebugSessionView(DataModel model,@GetParam("id") String id)
    {

        DebugSessionInfo key = mDebugActions.keySet().stream().filter(s -> s.getSessionId().equals(id)).findFirst().get();
        List<DebugSessionNavigationInfo> list = mDebugActions.get(key);

        model.addAttribute("id", id);
        model.addAttribute("list", list);



        return new AresViewBag(model);
    }

}

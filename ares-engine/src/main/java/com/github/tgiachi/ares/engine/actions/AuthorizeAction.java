package com.github.tgiachi.ares.engine.actions;

import com.github.tgiachi.ares.annotations.actions.*;
import com.github.tgiachi.ares.annotations.container.AresInject;
import com.github.tgiachi.ares.data.actions.AresViewBag;
import com.github.tgiachi.ares.data.template.DataModel;
import com.github.tgiachi.ares.data.template.RedirectResult;
import com.github.tgiachi.ares.engine.utils.EngineConst;
import com.github.tgiachi.ares.interfaces.actions.IAresAction;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpSession;

/**
 * Action per autorizzare la session
 */
@AresAction(name="authorize",baseUrl = "/auth")
public class AuthorizeAction implements IAresAction {


    @AresInject
    private Logger logger;

    @MapRequest(path = "/login")
    public AresViewBag doHomepage(DataModel model)
    {
        return new AresViewBag("/auth/login.tpl", model);
    }

    @MapRequest(path = "/login_post", type = RequestType.POST)
    public RedirectResult doLoginPost(DataModel model,
                                      @GetSessionParam(EngineConst.SESSION_PRE_AUTH) String prevUrl,
                                      @GetParam("email") String email,
                                      @GetParam("password") String password, HttpSession session)
    {
        session.setAttribute(EngineConst.SESSION_USER_AUTHENTICATED, "true");


        logger.info(String.format("Email => %s Password => %s logged",email,password));

        session.setAttribute("email", email);
        session.setAttribute("password", password);


        return new RedirectResult(prevUrl);
    }

    @MapRequest(path = "/logout", type = RequestType.GET)
    public RedirectResult doLogout(DataModel model, HttpSession session)
    {
        session.setAttribute(EngineConst.SESSION_USER_AUTHENTICATED, "false");

        return new RedirectResult("/");
    }
}

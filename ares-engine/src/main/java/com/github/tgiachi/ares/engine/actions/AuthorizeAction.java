package com.github.tgiachi.ares.engine.actions;

import com.github.tgiachi.ares.annotations.actions.*;
import com.github.tgiachi.ares.annotations.container.AresInject;
import com.github.tgiachi.ares.data.actions.AresViewBag;
import com.github.tgiachi.ares.data.auth.AuthDataResult;
import com.github.tgiachi.ares.data.auth.AuthUser;
import com.github.tgiachi.ares.data.template.DataModel;
import com.github.tgiachi.ares.data.template.JsonResult;
import com.github.tgiachi.ares.data.template.RedirectResult;
import com.github.tgiachi.ares.engine.utils.EngineConst;
import com.github.tgiachi.ares.interfaces.actions.IAresAction;
import com.github.tgiachi.ares.interfaces.auth.IAuthProcessor;
import com.github.tgiachi.ares.interfaces.engine.IAresEngine;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Action per autorizzare la session
 */
@AresAction(name="authorize",baseUrl = "/auth")
public class AuthorizeAction implements IAresAction {


    @AresInject
    private Logger logger;

    @AresInject
    private IAresEngine engine;

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

            AuthUser user = authenticateUser(email,password);

            if (user != null) {

                session.setAttribute(EngineConst.SESSION_USER_AUTHENTICATED, "true");
                session.setAttribute(EngineConst.SESSION_USER_DATA, user);

                session.setAttribute("email", email);
                session.setAttribute("password", password);
            }


        return new RedirectResult(prevUrl);
    }



    @MapRequest(path = "/login_post.json", type = RequestType.POST)
    public JsonResult doLoginPostJson(DataModel model,
                                      @GetSessionParam(EngineConst.SESSION_PRE_AUTH) String prevUrl,
                                      @GetParam("email") String email,
                                      @GetParam("password") String password, HttpSession session)
    {


        AuthDataResult result = new AuthDataResult();
        AuthUser user = authenticateUser(email,password);
        result.setAuthenticated(false);
        result.setResult("NO");


        if (user != null) {



            session.setAttribute(EngineConst.SESSION_USER_AUTHENTICATED, "true");


            logger.info(String.format("JSON Auth => Email => %s Password => %s logged", email, password));

            session.setAttribute("email", email);
            session.setAttribute("password", password);

            result.setAuthenticated(true);
            result.setResult("OK");
        }

        return new JsonResult(result);
    }

    @MapRequest(path = "/logout", type = RequestType.GET)
    public RedirectResult doLogout(DataModel model, HttpSession session)
    {
        session.setAttribute(EngineConst.SESSION_USER_AUTHENTICATED, "false");

        return new RedirectResult("/");
    }

    private AuthUser authenticateUser(String username, String password)
    {
        List<IAuthProcessor> authorize = engine.getContainer().getBeansImplementInterface(IAuthProcessor.class);

        if (authorize.size() == 1) {

            IAuthProcessor processor = authorize.get(0);

            AuthUser user = processor.authenticate(username, password);

            return user;
        }

        return null;

    }
}

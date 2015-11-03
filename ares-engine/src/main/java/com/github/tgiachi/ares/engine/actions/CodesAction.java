package com.github.tgiachi.ares.engine.actions;

import com.github.tgiachi.ares.annotations.actions.AresAction;
import com.github.tgiachi.ares.annotations.actions.AresCodeResult;
import com.github.tgiachi.ares.annotations.actions.MapRequest;
import com.github.tgiachi.ares.data.actions.AresViewBag;
import com.github.tgiachi.ares.data.template.DataModel;
import com.github.tgiachi.ares.interfaces.actions.IAresAction;

import javax.servlet.http.HttpServletResponse;

/**
 * Action per impostare i codici di default
 */
@AresAction(name = "codes", baseUrl = "/codes")
public class CodesAction implements IAresAction
{

    @AresCodeResult(HttpServletResponse.SC_NOT_FOUND)
    @MapRequest(path = "/404.html")
    public AresViewBag do404(DataModel model)
    {
        return new AresViewBag("/codes/404.tpl", model);
    }

    @AresCodeResult(HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
    @MapRequest(path = "/error.html")
    public AresViewBag doError(DataModel model)
    {
        return new AresViewBag("/codes/error.tpl",model);
    }


}

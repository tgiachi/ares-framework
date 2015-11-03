package com.github.tgiachi.ares.engine.actions;

import com.github.tgiachi.ares.annotations.actions.AresAction;
import com.github.tgiachi.ares.annotations.actions.AresProtectedArea;
import com.github.tgiachi.ares.annotations.actions.MapRequest;
import com.github.tgiachi.ares.data.actions.AresViewBag;
import com.github.tgiachi.ares.data.template.DataModel;
import com.github.tgiachi.ares.interfaces.actions.IAresAction;

/**
 * Action per testare la sicurezza con login
 */
@AresAction(name = "admin", baseUrl = "/admin")
@AresProtectedArea
public class AdminAction implements IAresAction {

    @MapRequest(path = "/")
    public AresViewBag doHomepage(DataModel model)
    {
        return new AresViewBag("admin/index.tpl", model);
    }
}

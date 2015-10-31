package com.github.tgiachi.ares.engine.actions;

import com.github.tgiachi.ares.annotations.actions.AresAction;
import com.github.tgiachi.ares.annotations.actions.MapRequest;
import com.github.tgiachi.ares.annotations.actions.RequestType;
import com.github.tgiachi.ares.data.actions.AresViewBag;
import com.github.tgiachi.ares.data.db.AresQuery;
import com.github.tgiachi.ares.data.template.DataModel;
import com.github.tgiachi.ares.data.template.JsonResult;
import com.github.tgiachi.ares.data.template.XmlResult;
import com.github.tgiachi.ares.data.template.YamlResult;
import com.github.tgiachi.ares.interfaces.actions.IAresAction;
import com.github.tgiachi.ares.sessions.SessionManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Action di prova
 */
@AresAction(name = "homepage")
public class TestAction implements IAresAction {

    @MapRequest(path = "/", type = RequestType.GET)
    public AresViewBag doHomepageGet(DataModel model)
    {
        model.addAttribute("title", "Today is " + new Date().toString());

        return new AresViewBag("index.tpl", model);
    }

    @MapRequest(path = "/api/data.json", type = RequestType.GET)
    public JsonResult doTestJson(DataModel model)
    {
        return new JsonResult(SessionManager.getConfig());
    }

    @MapRequest(path = "/api/data.xml", type = RequestType.GET)
    public XmlResult doTestXml(DataModel model)
    {
        return new XmlResult(SessionManager.getConfig());
    }

    @MapRequest(path = "/api/data.yaml", type = RequestType.GET)
    public YamlResult doTestYaml(DataModel model)
    {
        return new YamlResult(SessionManager.getConfig());
    }

    @MapRequest(path = "/pluto.html", type = RequestType.GET)
    public AresViewBag doPluto(DataModel model, AresQuery query)
    {
        List<String> tables = new ArrayList<>();
        try {
            query.getQuery().execute("select * from information_schema.tables;");
            while ( query.getQuery().getResultSet().next())
            {
                tables.add(query.getQuery().getResultSet().getString("table_name"));
            }

            model.addAttribute("tables", tables);

            return new AresViewBag("pluto.tpl", model);
        }
        catch (Exception ex)
        {

        }

        return null;
    }
}

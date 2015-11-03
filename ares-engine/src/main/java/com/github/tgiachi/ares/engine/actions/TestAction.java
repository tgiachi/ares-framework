package com.github.tgiachi.ares.engine.actions;

import com.github.tgiachi.ares.annotations.actions.*;
import com.github.tgiachi.ares.annotations.container.AresInject;
import com.github.tgiachi.ares.data.actions.AresViewBag;
import com.github.tgiachi.ares.data.db.AresQuery;
import com.github.tgiachi.ares.data.template.DataModel;
import com.github.tgiachi.ares.data.template.JsonResult;
import com.github.tgiachi.ares.data.template.XmlResult;
import com.github.tgiachi.ares.data.template.YamlResult;
import com.github.tgiachi.ares.engine.persistence.TableInfo;
import com.github.tgiachi.ares.engine.utils.EngineConst;
import com.github.tgiachi.ares.interfaces.actions.IAresAction;
import com.github.tgiachi.ares.sessions.SessionManager;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Action di prova
 */
@AresAction(name = "homepage")
public class TestAction implements IAresAction {


    @AresInject
    private Logger logger;

    @MapRequest(path = "/", type = RequestType.GET)
    public AresViewBag doHomepageGet(DataModel model, HttpSession session)
    {
        logger.log(Level.INFO, "Oh oh oh! Ares Inject works with actions");

        session.setMaxInactiveInterval(1);

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
            query.prepare("select * from information_schema.tables;");
            query.execute();

            List<TableInfo> results = query.mapResultToObject(TableInfo.class);

            //query.getResultSet().first();

            while ( query.getResultSet().next())
            {
                tables.add(query.getResultSet().getString("table_name"));
            }

            model.addAttribute("tables", tables);
            model.addAttribute("mapped", results);
            model.addAttribute("query_execution_time", query.getExecutionTime());

            return new AresViewBag("pluto.tpl", model);
        }
        catch (Exception ex)
        {

        }

        return null;
    }

    @MapRequest(path = "/testparam.html", type = RequestType.GET)
    public AresViewBag doTestInjectParam(DataModel model, @GetParam("q") String q)
    {
        model.addAttribute("param", q);
        return new AresViewBag("index_param.tpl", model);
    }

    @MapRequest(path = "/debug.html", type = RequestType.GET)
    public AresViewBag doViewStats(DataModel model, @GetSessionParam(EngineConst.SESSION_PREV_URL) String prevUrl )
    {
        model.addAttribute("generation_stats", SessionManager.getGenerationStats().stream().filter(s -> !s.getLog().equals("debug.tpl")).collect(Collectors.toList()));
        model.addAttribute("session_prev", prevUrl);
        return new AresViewBag("debug.tpl", model);
    }

    @MapRequest(path = "/debug_clear_logs.json", type = RequestType.GET)
    public JsonResult clearDebugLogs()
    {
        SessionManager.setGenerationStats(new ArrayList<>());
        return new JsonResult("OK");
    }
}

package com.github.tgiachi.ares.engine.actions;

import com.github.tgiachi.ares.annotations.actions.*;
import com.github.tgiachi.ares.annotations.container.AresInject;
import com.github.tgiachi.ares.data.actions.AresViewBag;
import com.github.tgiachi.ares.data.db.AresQuery;
import com.github.tgiachi.ares.data.template.DataModel;
import com.github.tgiachi.ares.data.template.JsonResult;
import com.github.tgiachi.ares.data.template.XmlResult;
import com.github.tgiachi.ares.data.template.YamlResult;
import com.github.tgiachi.ares.database.entities.TestEntity;
import com.github.tgiachi.ares.engine.persistence.TableInfo;
import com.github.tgiachi.ares.engine.utils.AppInfo;
import com.github.tgiachi.ares.engine.utils.EngineConst;
import com.github.tgiachi.ares.interfaces.actions.IAresAction;
import com.github.tgiachi.ares.interfaces.database.IOrmManager;
import com.github.tgiachi.ares.sessions.SessionManager;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;


/**
 * Action di prova
 */
@AresAction(name = "testaction")
public class TestAction implements IAresAction {


    @AresInject
    private Logger logger;

    @MapRequest(path = "/", type = RequestType.GET)
    public AresViewBag doHomepageGet(DataModel model, HttpSession session)
    {
        logger.log(Level.INFO, "Oh oh oh! Ares Inject works with actions");


        model.addAttribute("title", "Today is " + new Date().toString());

        return new AresViewBag("index.ftl", model);
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

    @MapRequest(path = "/testdatabase.html", type = RequestType.GET)
    public AresViewBag doPluto(DataModel model, AresQuery query)
    {
        List<String> tables = new ArrayList<>();
        try {
            query.prepare("select * from information_schema.tables;");
            query.execute();

            List<TableInfo> results = query.mapResultToObject(TableInfo.class);

            while ( query.getResultSet().next())
            {
                tables.add(query.getResultSet().getString("table_name"));
            }

            model.addAttribute("tables", tables);
            model.addAttribute("mapped", results);
            model.addAttribute("query_execution_time", query.getExecutionTime());

            return new AresViewBag("testdatabase.ftl", model);
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
        return new AresViewBag("index_param.ftl", model);
    }

    @MapRequest(path = "/testcookie.html", type = RequestType.GET)
    public AresViewBag doTestCookie(DataModel model, @GetCookie("JSESSIONID") Cookie jSessionCookie)
    {

        model.addAttribute("cookie", jSessionCookie.getValue());
        AresViewBag vb = new AresViewBag(model);
        Cookie cookie = new Cookie("test_cookie", "test1234");
        cookie.setMaxAge(100000);
        cookie.setSecure(false );
        vb.getCookies().add(cookie);
        return vb;

    }

    @MapRequest(path = "/testheader.html", type = RequestType.GET)
    public AresViewBag doTestHeader(DataModel model)
    {
        AresViewBag vb = new AresViewBag(model);

        vb.getHeaders().put("X-Powered-by", AppInfo.AppName + " v"+AppInfo.AppVersion);

        return vb;
    }

    @MapRequest(path = "/make_error.html", type = RequestType.GET)
    public AresViewBag doTestError(DataModel model)
    {
        return new AresViewBag("make_error.ftl", model);
    }

    @MapRequest(path = "/debug.html", type = RequestType.GET)
    public AresViewBag doViewStats(DataModel model, @GetSessionParam(EngineConst.SESSION_PREV_URL) String prevUrl )
    {
        model.addAttribute("generation_stats", SessionManager.getGenerationStats().stream().filter(s -> !s.getLog().equals("debug.tpl")).collect(Collectors.toList()));
        model.addAttribute("session_prev", prevUrl);
        return new AresViewBag("debug.ftl", model);
    }

    @MapRequest(path = "/test_view.html", type = RequestType.GET)
    public AresViewBag doTestAutomaticView(DataModel model)
    {
        model.addAttribute("test", 123);

        return new AresViewBag(model);
    }

    @MapRequest(path = "/test_orm.html", type = RequestType.GET)
    public AresViewBag doPersistenceTest(DataModel model, IOrmManager ormManager)
    {
        List<?> entities = ormManager.executeQuery("from TestEntity");

        if (entities.size() == 0) {
            for (int i = 0; i < 300; i++) {
                TestEntity entity = new TestEntity();
                entity.setField1("Index : " + i);
                entity.setField2(new Random().nextInt());
                entity.setField3(new Random().nextLong());

                ormManager.persist(entity);

            }

            entities = ormManager.executeQuery("from TestEntity");
        }

        model.addAttribute("result", entities );

        return new AresViewBag(model);
    }

    @MapRequest(path = "/test_orm.json", type = RequestType.GET)
    public JsonResult doPersistenceTestJson(IOrmManager ormManager)
    {
        List<?> entities = ormManager.executeQuery("from TestEntity");

        if (entities.size() == 0) {
            for (int i = 0; i < 300; i++) {
                TestEntity entity = new TestEntity();
                entity.setField1("Index : " + i);
                entity.setField2(new Random().nextInt());
                entity.setField3(new Random().nextLong());

                ormManager.persist(entity);

            }

            entities = ormManager.executeQuery("from TestEntity");
        }


        return new JsonResult(entities);
    }

    @MapRequest(path = "/test_orm.xml", type = RequestType.GET)
    public XmlResult doPersistenceTestXml(IOrmManager ormManager)
    {
        List<?> entities = ormManager.executeQuery("from TestEntity");

        if (entities.size() == 0) {
            for (int i = 0; i < 300; i++) {
                TestEntity entity = new TestEntity();
                entity.setField1("Index : " + i);
                entity.setField2(new Random().nextInt());
                entity.setField3(new Random().nextLong());

                ormManager.persist(entity);

            }

            entities = ormManager.executeQuery("from TestEntity");
        }


        return new XmlResult(entities);
    }

    @MapRequest(path = "/debug_clear_logs.json", type = RequestType.GET)
    public JsonResult clearDebugLogs()
    {
        SessionManager.setGenerationStats(new ArrayList<>());
        return new JsonResult("OK");
    }


    @MapRequest(path = "/test_ws.html")
    public AresViewBag doWebSocketClient(DataModel dataModel)
    {
        return new AresViewBag(dataModel);
    }

    @MapRequest(path = "/view_action_map.html", type = RequestType.GET)
    public AresViewBag doViewMap(DataModel model)
    {
        model.addAttribute("map", SessionManager.getEngine().getDispatcher().getActionsMap());

        return  new AresViewBag(model);
    }

    public JsonResult doAbout()
    {
        return new JsonResult("Ares framework");
    }
}

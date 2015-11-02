package com.github.tgiachi.ares.engine.servlet;

import com.github.tgiachi.ares.annotations.actions.RequestType;
import com.github.tgiachi.ares.data.actions.ServletResult;
import com.github.tgiachi.ares.engine.utils.AppInfo;
import com.github.tgiachi.ares.sessions.SessionManager;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.HashMap;

/**
 * Servlet che permette di usare Ares
 */
public class AresServlet extends HttpServlet {

    private static Logger logger = Logger.getLogger(AresServlet.class);

    @Override
    public void log(String msg) {
        logger.log(Level.INFO, msg);
        super.log(msg);
    }

    public AresServlet()
    {
        logger.info(AppInfo.AppHeader);
        logger.info(String.format("%s v%s", AppInfo.AppName, AppInfo.AppVersion));
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        process(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        process(req, resp);
    }

    @Override
    protected void doHead(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        process(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        process(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        process(req, resp);
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        process(req, resp);
    }

    @Override
    protected void doTrace(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        process(req, resp);
    }

    private void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {

        String request_url = req.getRequestURI().replace(req.getContextPath() , "");

        ServletResult result =  SessionManager.getEngine().getDispatcher().dispach(request_url, getRequestTypeByString(req.getMethod()), parseHeaders(req), parseQueryString(req.getQueryString()), req);

        if (result.getReturnCode() == HttpServletResponse.SC_OK) {

            resp.setContentType(result.getMimeType());
            resp.setContentLength(result.getResult().length);

            IOUtils.write(result.getResult(), resp.getOutputStream());
        }
        else
        {
            resp.sendError(result.getReturnCode());
        }
    }

    protected HashMap<String, String> parseQueryString(String queryString)
    {
        HashMap<String, String> result = new HashMap<>();

        if (queryString != null) {
            String[] params = queryString.split("&");

            for (String param : params) {
                result.put(param.split("=")[0], param.split("=")[1]);
            }
        }
        return result;
    }

    protected HashMap<String, String> parseHeaders(HttpServletRequest request)
    {
        HashMap<String, String> result = new HashMap<>();

        for(String headerName : Collections.list(request.getHeaderNames()))
        {
            result.put(headerName, request.getHeader(headerName));
        }

        return result;

    }

    private RequestType getRequestTypeByString(String requestType)
    {
        return RequestType.valueOf(requestType);
    }

}

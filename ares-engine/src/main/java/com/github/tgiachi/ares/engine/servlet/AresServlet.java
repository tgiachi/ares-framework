package com.github.tgiachi.ares.engine.servlet;

import com.github.tgiachi.ares.annotations.actions.RequestType;
import com.github.tgiachi.ares.data.actions.ServletResult;
import com.github.tgiachi.ares.sessions.SessionManager;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import javax.activation.MimeType;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

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

    }



    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        String request_url = req.getRequestURI().replace(req.getContextPath() , "");

        log(String.format("[%s] - GET - %s", req.getRemoteAddr(), request_url));
        ServletResult result =  SessionManager.getEngine().getDispacher().dispach(request_url, RequestType.GET, parseHeaders(req), parseQueryString(req.getQueryString()),req);


        resp.setContentType(result.getMimeType());
        PrintWriter out = resp.getWriter();

        out.print((String)result.getResult());

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        SessionManager.getEngine().getDispacher().dispach(req.getRequestURI(), RequestType.POST, parseHeaders(req),parseQueryString(req.getQueryString()),req);
    }

    @Override
    protected void doHead(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SessionManager.getEngine().getDispacher().dispach(req.getRequestURI(), RequestType.HEAD, parseHeaders(req), parseQueryString(req.getQueryString()),req);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SessionManager.getEngine().getDispacher().dispach(req.getRequestURI(), RequestType.DELETE, parseHeaders(req), parseQueryString(req.getQueryString()),req);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SessionManager.getEngine().getDispacher().dispach(req.getRequestURI(), RequestType.PUT, parseHeaders(req), new HashMap<>(),req);
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

}

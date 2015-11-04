package com.github.tgiachi.ares.engine.resultparsers;

import com.github.tgiachi.ares.annotations.resultsparsers.AresResultParser;
import com.github.tgiachi.ares.data.actions.ServletResult;
import com.github.tgiachi.ares.data.template.DataModel;
import com.github.tgiachi.ares.data.template.RedirectResult;
import com.github.tgiachi.ares.engine.resultparsers.base.BaseResultParser;

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * Esegue redirect
 */
@AresResultParser(RedirectResult.class)
public class RedirectResultParser extends BaseResultParser {

    @Override
    public ServletResult parse(DataModel model, Method method, Object invoker, Object[] params) throws Exception {

        RedirectResult redirectResult = (RedirectResult) method.invoke(invoker, params);

        ServletResult result = new ServletResult(HttpServletResponse.SC_MOVED_PERMANENTLY);
        result.setResult(redirectResult.getLocation().getBytes());

        return result;
    }
}

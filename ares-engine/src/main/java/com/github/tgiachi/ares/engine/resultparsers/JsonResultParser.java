package com.github.tgiachi.ares.engine.resultparsers;

import com.github.tgiachi.ares.annotations.resultsparsers.AresResultParser;
import com.github.tgiachi.ares.data.actions.ServletResult;
import com.github.tgiachi.ares.data.template.DataModel;
import com.github.tgiachi.ares.data.template.JsonResult;
import com.github.tgiachi.ares.engine.resultparsers.base.BaseResultParser;
import com.github.tgiachi.ares.engine.serializer.JsonSerializer;

import java.lang.reflect.Method;

/**
 * Implementa la serializzazione JSon sul result
 */
@AresResultParser(JsonResult.class)
public class JsonResultParser  extends BaseResultParser{
    @Override
    public ServletResult parse(DataModel model, Method method, Object invoker, Object[] params) throws Exception {

        JsonResult result =(JsonResult) method.invoke(invoker, params);
        ServletResult servletResult = new ServletResult("application/json", JsonSerializer.Serialize(result.getData()).getBytes());

        servletResult.setCookies(result.getCookies());
        servletResult.setHeaders(result.getHeaders());

        return servletResult;

    }
}

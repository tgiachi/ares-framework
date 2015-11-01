package com.github.tgiachi.ares.engine.resultparsers;

import com.github.tgiachi.ares.annotations.resultsparsers.AresResultParser;
import com.github.tgiachi.ares.data.actions.ServletResult;
import com.github.tgiachi.ares.data.template.DataModel;
import com.github.tgiachi.ares.data.template.JsonResult;
import com.github.tgiachi.ares.data.template.XmlResult;
import com.github.tgiachi.ares.engine.resultparsers.base.BaseResultParser;
import com.github.tgiachi.ares.engine.serializer.JsonSerializer;
import com.github.tgiachi.ares.engine.serializer.XmlSerializer;

import java.lang.reflect.Method;

/**
 * Classe per XML tempplate result
 */
@AresResultParser(XmlResult.class)
public class XmlResultParser extends BaseResultParser {

    public ServletResult parse(DataModel model, Method method, Object invoker, Object[] params) throws Exception {

        XmlResult result =(XmlResult) method.invoke(invoker, params);

        return new ServletResult("application/xml", XmlSerializer.toXml(result.getData()).getBytes());

    }
}

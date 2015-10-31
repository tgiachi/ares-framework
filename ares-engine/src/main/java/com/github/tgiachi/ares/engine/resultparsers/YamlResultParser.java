package com.github.tgiachi.ares.engine.resultparsers;

import com.github.tgiachi.ares.annotations.resultsparsers.AresResultParser;
import com.github.tgiachi.ares.data.actions.ServletResult;
import com.github.tgiachi.ares.data.template.DataModel;
import com.github.tgiachi.ares.data.template.XmlResult;
import com.github.tgiachi.ares.data.template.YamlResult;
import com.github.tgiachi.ares.engine.resultparsers.base.BaseResultParser;
import com.github.tgiachi.ares.engine.serializer.XmlSerializer;
import com.github.tgiachi.ares.engine.serializer.YAMLSerializer;

import java.lang.reflect.Method;

/**
 * Yaml Result parser
 */
@AresResultParser(YamlResult.class)
public class YamlResultParser extends BaseResultParser {

    public ServletResult parse(DataModel model, Method method, Object invoker, Object[] params) throws Exception {

        YamlResult result =(YamlResult) method.invoke(invoker, params);

        return new ServletResult("application/x+yaml", YAMLSerializer.toYaml(result.getData()));

    }
}

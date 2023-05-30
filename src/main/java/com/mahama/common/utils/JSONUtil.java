package com.mahama.common.utils;

import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.PropertyFilter;

public class JSONUtil {
    public static PropertyFilter auditorFilter(){
        return  (source, name, value) -> !StringUtil.checkRegex(name,"(modify|create)\\w+");
    }

    public static ParserConfig camelCase() {
        ParserConfig parserConfig = new ParserConfig();
        parserConfig.propertyNamingStrategy = PropertyNamingStrategy.CamelCase;
        return parserConfig;
    }

    public static ParserConfig snakeCase() {
        ParserConfig parserConfig = new ParserConfig();
        parserConfig.propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;
        return parserConfig;
    }
}

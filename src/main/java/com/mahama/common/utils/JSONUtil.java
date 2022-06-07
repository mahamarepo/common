package com.mahama.common.utils;

import com.alibaba.fastjson.serializer.PropertyFilter;

public class JSONUtil {
    public static PropertyFilter auditorFilter(){
        return  (source, name, value) -> !StringUtil.checkRegex(name,"(modify|create)\\w+");
    }
}
